package mod.syconn.swm.client;

import mod.syconn.swm.client.render.item.HoloProjectorItemRenderer;
import mod.syconn.swm.features.blaster.client.BlasterItemRenderer;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.features.lightsaber.client.item.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.item.HoloProjectorItem;
import mod.syconn.swm.mixin.client.MinecraftAccessor;
import mod.syconn.swm.registry.ModMenus;
import mod.syconn.swm.utils.client.TintedTextureProvider;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import mod.syconn.swm.utils.interfaces.IModifiedPoseRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class StarWarsClient {

    public static TintedTextureProvider tintedTextureProvider = new TintedTextureProvider();

    public static void init() {
        IModifiedItemRenderer.register(LightsaberItem.class, new LightsaberItemRender());
        IModifiedItemRenderer.register(HoloProjectorItem.class, new HoloProjectorItemRenderer());
        IModifiedItemRenderer.register(BlasterItem.class, new BlasterItemRenderer());

        IModifiedPoseRenderer.register(LightsaberItem.class, new LightsaberItemRender());
        IModifiedPoseRenderer.register(HoloProjectorItem.class, new HoloProjectorItemRenderer());
        IModifiedPoseRenderer.register(BlasterItem.class, new BlasterItemRenderer());

//        ModKeys.KEYS.forEach(KeyMappingRegistry::register);

//        EntityRendererRegistry.register(ModEntities.THROWN_LIGHTSABER, ThrownLightsaberRenderer::new); TODO
//        EntityRendererRegistry.register(ModEntities.BLASTER_BOLT, BlasterBoltRenderer::new);
//        EntityRendererRegistry.register(ModEntities.BLASTER_ION_BOLT, BlasterBoltRenderer::new);
//        EntityRendererRegistry.register(ModEntities.BLASTER_STUN_BOLT, BlasterBoltRenderer::new);

//        ClientLifecycleEvent.CLIENT_SETUP.register(StarWarsClient::setupEvent); TODO
//        ClientGuiEvent.RENDER_HUD.register(ClientHooks::renderHUD);
//        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(Constants.TRACKER::clientPlayerJoined);
//        SOME_TICK_EVENT
    }

    public static void setupEvent(Minecraft minecraft) {
//        BlockEntityRendererRegistry.register(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchRenderer::new); TODO
//        BlockEntityRendererRegistry.register(ModBlockEntities.HOLO_PROJECTOR.get(), HoloProjectorBlockEntityRenderer::new);

        ModMenus.registerScreens();
    }

    public static void onClientTick(LocalPlayer player) {
        KeyHandler.handleKeyMappings(player);
    }

    public static float getTickDelta() {
        var mc = Minecraft.getInstance();
        if (mc.isPaused()) return ((MinecraftAccessor)mc).getPausedTickDelta();
        return mc.getFrameTime();
    }

    public static ResourceLocation tintTexture(ResourceLocation texture, int color) {
        var textureId = texture.getNamespace() + "/" + texture.getPath() + "/" + Integer.toHexString(color);
        return StarWarsClient.tintedTextureProvider.tint(textureId, texture, color);
    }
}
