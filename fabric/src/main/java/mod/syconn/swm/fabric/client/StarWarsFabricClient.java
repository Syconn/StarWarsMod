package mod.syconn.swm.fabric.client;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.client.render.entity.layers.SWGearLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public final class StarWarsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((entityType, livingEntityRenderer, registrationHelper, context) -> {
            if(livingEntityRenderer instanceof PlayerRenderer renderer) registrationHelper.register(new SWGearLayer<>(renderer, context.getItemRenderer()));
        }));

        ClientTickEvents.END_CLIENT_TICK.register(client -> StarWarsClient.onClientTick(client.player));
    }
}
