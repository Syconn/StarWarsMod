package mod.syconn.swm;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import mod.syconn.swm.addons.LightsaberContent;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.ModComponents;
import mod.syconn.swm.core.ModEntities;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.server.StarWarsServer;
import mod.syconn.swm.util.server.SyncedResourceManager;

public final class StarWars {
    public static void init() {
        ModComponents.DATA_COMPONENTS.register();
        ModItems.ITEMS.register();
        ModItems.TABS.register();
        ModEntities.ENTITIES.register();

        SyncedResourceManager.register(LightsaberContent.LIGHTSABER_DATA);

        EnvExecutor.runInEnv(Env.CLIENT, () -> StarWarsClient::init);
        EnvExecutor.runInEnv(Env.SERVER, () -> StarWarsServer::init);
    }
}
