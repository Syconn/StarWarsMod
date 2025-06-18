package mod.syconn.swm.fabric.client;

import mod.syconn.swm.client.StarWarsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class StarWarsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> StarWarsClient.onClientTick(client.player));
    }
}
