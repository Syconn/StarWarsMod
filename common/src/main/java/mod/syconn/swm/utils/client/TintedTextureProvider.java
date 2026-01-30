package mod.syconn.swm.utils.client;

import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TintedTextureProvider extends TextureProvider<TintedResourceLocation> {

    public static final ResourceLocation ROOT = Constants.withId("///tinted");

    public TintedTextureProvider() {
        super(ROOT);
    }

    public ResourceLocation tint(String textureId, ResourceLocation texture, int color) {
        return getId(textureId, () -> texture, () -> new TintedResourceLocation(texture.getNamespace(), texture.getPath(), ColorUtil.argbToAbgr(color)));
    }

    @Override
    protected CallbackTexture createTexture(ResourceLocation destId, TintedResourceLocation request, Consumer<Boolean> callback) {
        registerDependencyCallbacks(destId, request);
        return new TintedTexture(request, DefaultPlayerSkin.getDefaultSkin(), callback);
    }
}
