package mod.syconn.swm.fabric;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.server.StarWarsServer;
import net.fabricmc.api.*;

public final class StarWarsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StarWars.init();
    }

    @Environment(EnvType.CLIENT)
    public static final class StarWarsClientFabric implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            StarWarsClient.init();
        }
    }

    @Environment(EnvType.SERVER)
    public static final class StarWarsServerFabric implements DedicatedServerModInitializer {

        @Override
        public void onInitializeServer() {
            StarWarsServer.init();
        }
    }
}
