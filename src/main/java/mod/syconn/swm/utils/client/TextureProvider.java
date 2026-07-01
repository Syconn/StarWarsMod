package mod.syconn.swm.utils.client;

import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swm.utils.Constants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TextureProvider<TData> {
    private static final ArrayList<ResourceLocation> TEXTURE_CACHE = new ArrayList<>();
    private static final ArrayList<ResourceLocation> FAILURE_CACHE = new ArrayList<>();
    private static final HashMap<ResourceLocation, List<Consumer<Boolean>>> LOAD_CALLBACKS = new HashMap<>();
    private static final ArrayList<TextureProvider<?>> TEXTURE_PROVIDERS = new ArrayList<>();

    private final ResourceLocation root;

    protected TextureProvider(ResourceLocation cacheIdRoot) {
        this.root = cacheIdRoot;

        TEXTURE_PROVIDERS.add(this);
    }

    @NotNull
    protected ResourceLocation createCacheId(String requestName) {
        return new ResourceLocation(root.getNamespace(), root.getPath() + "/" + requestName);
    }

    public boolean isProviderFor(ResourceLocation cacheId) {
        return cacheId.getNamespace().equals(root.getNamespace()) && cacheId.getPath().startsWith(root.getPath());
    }

    public ResourceLocation getProvidedId(ResourceLocation cacheId) {
        // If we provided this ID, return it
        if (isProviderFor(cacheId))
            return cacheId;

        // Also return if we provided the fallback ID in a FallbackResourceLocation
        if (cacheId instanceof FallbackResourceLocation fi && isProviderFor(fi.getSource())) return fi.getSource();
        return null;
    }

    public boolean isReady(ResourceLocation cacheId) {
        if (!TEXTURE_CACHE.contains(cacheId)) return false;
        Minecraft.getInstance().getTextureManager().getTexture(cacheId, null);
        return true;
    }

    protected void markTextureDirty(ResourceLocation cacheId) {
        TEXTURE_CACHE.removeIf(cacheId::equals);
    }

    protected void registerDependencyCallbacks(ResourceLocation cacheId, ResourceLocation dependencyCacheId) {
        for (var provider : TEXTURE_PROVIDERS) {
            var providerCacheId = provider.getProvidedId(dependencyCacheId);

            if (providerCacheId != null && !provider.isReady(dependencyCacheId)) {
                provider.addLoadCallback(providerCacheId, (success) -> {
                    if (success) markTextureDirty(cacheId);
                });
                break;
            }
        }
    }

    public ResourceLocation getId(String requestName, Supplier<ResourceLocation> fallback, Supplier<TData> requestFulfiller) {
        var cacheId = createCacheId(requestName);
        if (FAILURE_CACHE.contains(cacheId)) return fallback == null ? cacheId : fallback.get();
        var texture = Minecraft.getInstance().getTextureManager().getTexture(cacheId, null);
        if (!TEXTURE_CACHE.contains(cacheId)) {
            bakeTextureAsync(cacheId, requestFulfiller.get());
            TEXTURE_CACHE.add(cacheId);
        }

        if (fallback == null) return cacheId;
        var fallbackId = fallback.get();
        return new FallbackResourceLocation(fallbackId.getNamespace(), fallbackId.getPath(), cacheId);
    }

    protected abstract CallbackTexture createTexture(ResourceLocation destId, TData requestData, Consumer<Boolean> callback);

    protected void bakeTextureAsync(ResourceLocation cacheId, TData request) {
        Util.backgroundExecutor().execute(() -> {
            try {
                var minecraft = Minecraft.getInstance();
                minecraft.execute(() -> RenderSystem.recordRenderCall(() -> registerTexture(cacheId, request)));
            } catch (InsecurePublicKeyException insecureTextureException) {
                Constants.LOG.warn("Attempt to load insecure texture blocked: {}", cacheId, insecureTextureException);
            }
        });
    }

    protected void registerTexture(ResourceLocation cacheId, TData request) {
        Constants.LOG.debug("Registering texture {}", cacheId);
        Minecraft.getInstance().getTextureManager().register(cacheId, createTexture(cacheId, request, success -> pollCallbacks(cacheId, success)));
    }

    public void addLoadCallback(ResourceLocation target, Consumer<Boolean> callback) {
        if (isReady(target)) {
            callback.accept(true);
            return;
        }

        if (!LOAD_CALLBACKS.containsKey(target)) LOAD_CALLBACKS.put(target, new ArrayList<>());

        LOAD_CALLBACKS.get(target).add(callback);
    }

    private void pollCallbacks(ResourceLocation identifier, boolean success) {
        var callbacks = LOAD_CALLBACKS.get(identifier);
        if (callbacks == null) return;

        callbacks.forEach(callback -> callback.accept(success));
        LOAD_CALLBACKS.remove(identifier);
    }
}
