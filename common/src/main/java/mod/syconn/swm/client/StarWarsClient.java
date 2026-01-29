package mod.syconn.swm.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import mod.syconn.swm.client.render.block.HoloProjectorBlockEntityRenderer;
import mod.syconn.swm.client.render.item.HoloProjectorItemRenderer;
import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.core.ModEntities;
import mod.syconn.swm.core.ModKeys;
import mod.syconn.swm.core.ModMenus;
import mod.syconn.swm.features.blaster.client.BlasterItemRenderer;
import mod.syconn.swm.features.blaster.client.entity.BlasterBoltRenderer;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.features.lightsaber.client.block.LightsaberWorkbenchRenderer;
import mod.syconn.swm.features.lightsaber.client.entity.ThrownLightsaberRenderer;
import mod.syconn.swm.features.lightsaber.client.item.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.item.HoloProjectorItem;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import mod.syconn.swm.utils.interfaces.IModifiedPoseRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public class StarWarsClient {

    public static void init() {
        IModifiedItemRenderer.register(LightsaberItem.class, new LightsaberItemRender());
        IModifiedItemRenderer.register(HoloProjectorItem.class, new HoloProjectorItemRenderer());
        IModifiedItemRenderer.register(BlasterItem.class, new BlasterItemRenderer());

        IModifiedPoseRenderer.register(LightsaberItem.class, new LightsaberItemRender());
        IModifiedPoseRenderer.register(HoloProjectorItem.class, new HoloProjectorItemRenderer());
        IModifiedPoseRenderer.register(BlasterItem.class, new BlasterItemRenderer());

        ModKeys.KEYS.forEach(KeyMappingRegistry::register);

        EntityRendererRegistry.register(ModEntities.THROWN_LIGHTSABER, ThrownLightsaberRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLASTER_BOLT, BlasterBoltRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLASTER_ION_BOLT, BlasterBoltRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLASTER_STUN_BOLT, BlasterBoltRenderer::new);

        ClientLifecycleEvent.CLIENT_SETUP.register(StarWarsClient::setupEvent);
        ClientGuiEvent.RENDER_HUD.register(ClientHooks::renderHUD);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(Constants.TRACKER::clientPlayerJoined);
    }

    public static void setupEvent(Minecraft minecraft) {
        BlockEntityRendererRegistry.register(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.HOLO_PROJECTOR.get(), HoloProjectorBlockEntityRenderer::new);

        ModMenus.registerScreens();
    }

    public static void onClientTick(LocalPlayer player) {
        KeyHandler.handleKeyMappings(player);
    }

    public static float getTickDelta() {
        return Minecraft.getInstance().getDeltaFrameTime();
    }
}
