package mod.syconn.swm.client;

import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.keys.KeyHandler;
import mod.syconn.swm.core.*;
import mod.syconn.swm.features.lightsaber.client.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.client.entity.LightsaberWorkbenchRenderer;
import mod.syconn.swm.features.lightsaber.client.entity.ThrownLightsaberRenderer;
import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.client.render.IModifiedItemRenderer;
import mod.syconn.swm.util.client.render.IModifiedPoseRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class StarWarsClient {

    public static void init() {
        IModifiedItemRenderer.register(LightsaberItem.class, new LightsaberItemRender());

        IModifiedPoseRenderer.register(LightsaberItem.class, new LightsaberItemRender());

        ItemPropertiesRegistry.register(ModItems.LIGHTSABER.get(), Constants.withId("model"),
                ((stack, level, holder, seed) -> (float) LightsaberComponent.getOrCreate(stack).model() * 0.1f));

        KeyMappingRegistry.register(ModKeys.TOGGLE_ITEM);
        KeyMappingRegistry.register(ModKeys.POWER_1);

        EntityRendererRegistry.register(ModEntities.THROWN_LIGHTSABER, ThrownLightsaberRenderer::new);

        BlockEntityRendererRegistry.register(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchRenderer::new);

        TickEvent.PLAYER_PRE.register(StarWarsClient::onClientTick);

        ModMenus.registerScreens();

        Network.registerReceivers();
    }

    public static void onClientTick(Player player) {
        KeyHandler.handleKeyMappings(player);
    }

    public static float getTickDelta() {
        return GameInstance.getClient().getTimer().getGameTimeDeltaPartialTick(true);
    }
}
