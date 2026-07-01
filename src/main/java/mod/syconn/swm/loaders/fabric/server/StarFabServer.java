package mod.syconn.swm.loaders.fabric.server;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.server.StarWarsServer;
import net.fabricmc.api.DedicatedServerModInitializer;

public class StarFabServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        StarWarsServer.init();
    }
}
