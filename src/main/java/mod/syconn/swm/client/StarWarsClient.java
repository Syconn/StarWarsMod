package mod.syconn.swm.client;

import mod.syconn.swm.api.registry.client.*;
import mod.syconn.swm.api.util.Env;
import mod.syconn.swm.api.util.Environment;
import mod.syconn.swm.client.screen.hud.SWGearHud;
import mod.syconn.swm.features.blaster.client.BlasterItemRenderer;
import mod.syconn.swm.features.blaster.client.entity.BlasterBoltRenderer;
import mod.syconn.swm.features.blaster.client.particle.ScorchParticle;
import mod.syconn.swm.features.blaster.client.particle.SlugTrailParticle;
import mod.syconn.swm.features.blaster.client.particle.SparkParticle;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.features.lightsaber.client.block.LightsaberWorkbenchRenderer;
import mod.syconn.swm.features.lightsaber.client.entity.ThrownLightsaberRenderer;
import mod.syconn.swm.features.lightsaber.client.item.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.client.screen.LightsaberAssemblerScreen;
import mod.syconn.swm.features.lightsaber.client.screen.LightsaberWorkbenchScreen;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.mixin.client.MinecraftAccessor;
import mod.syconn.swm.registry.ModBlockEntities;
import mod.syconn.swm.registry.ModEntities;
import mod.syconn.swm.registry.ModMenus;
import mod.syconn.swm.registry.ModParticles;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.TintedTextureProvider;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import mod.syconn.swm.utils.interfaces.IModifiedPoseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class StarWarsClient {

    public static TintedTextureProvider tintedTextureProvider = new TintedTextureProvider();

    public static void init() {
        IModifiedItemRenderer.register(LightsaberItem.class, new LightsaberItemRender());
        IModifiedItemRenderer.register(BlasterItem.class, new BlasterItemRenderer());

        IModifiedPoseRenderer.register(LightsaberItem.class, new LightsaberItemRender());
        IModifiedPoseRenderer.register(BlasterItem.class, new BlasterItemRenderer());

        Environment.setExecutor(Env.CLIENT, Minecraft.getInstance());
    }

    public static void registerScreens(ScreenRegister register) {
        register.apply(ModMenus.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchScreen::new);
        register.apply(ModMenus.LIGHTSABER_ASSEMBLER.get(), LightsaberAssemblerScreen::new);
    }

    public static void registerBlockEntityRenderers(BlockEntityRendererRegister register) {
        register.apply(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchRenderer::new);
    }

    public static void registerEntityRenderers(EntityRendererRegister register) {
        register.apply(ModEntities.THROWN_LIGHTSABER.get(), ThrownLightsaberRenderer::new);
        register.apply(ModEntities.BLASTER_BOLT.get(), BlasterBoltRenderer::new);
        register.apply(ModEntities.BLASTER_ION_BOLT.get(), BlasterBoltRenderer::new);
        register.apply(ModEntities.BLASTER_STUN_BOLT.get(), BlasterBoltRenderer::new);
    }

    public static void registerRenderTypes(RenderTypeRegister register) {

    }

    public static void registerParticleProviders(ParticleProviderRegister register) {
        register.apply(ModParticles.SCORCH.get(), ScorchParticle.Provider::new);
        register.apply(ModParticles.SLUG_TRAIL.get(), SlugTrailParticle.Provider::new);
        register.apply(ModParticles.SPARK.get(), SparkParticle.Provider::new);
    }

    public static void registerBlockColors(BlockColorsRegister register) {
    }

    public static void registerItemColors(ItemColorsRegister register) {
    }

    public static void registerHudOverlays(HudOverlayRegister register) {
        register.apply(Constants.withId("sw_gear"), new SWGearHud());
    }

    public static void onClientTick(Player player) {
        if (player instanceof LocalPlayer lp) KeyHandler.handleKeyMappings(lp);
    }

    public static float getTickDelta() {
        var mc = Minecraft.getInstance();
        if (mc.isPaused()) return ((MinecraftAccessor) mc).getPausedTickDelta();
        return mc.getFrameTime();
    }

    public static ResourceLocation tintTexture(ResourceLocation texture, int color) {
        var textureId = texture.getNamespace() + "/" + texture.getPath() + "/" + Integer.toHexString(color);
        return StarWarsClient.tintedTextureProvider.tint(textureId, texture, color);
    }
}
