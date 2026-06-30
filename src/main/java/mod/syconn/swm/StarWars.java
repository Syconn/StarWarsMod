package mod.syconn.swm;

import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.server.StarWarsServer;
import mod.syconn.swm.utils.Constants;
import net.minecraft.server.packs.PackType;

public class StarWars {

    public static void initialize() {
        Registrar.loadRegistries();
        Network.init();

//        ReloadListenerRegistry.register(PackType.SERVER_DATA, LightsaberContent.LIGHTSABER_DATA, Constants.withId("lightsaber_data")); TODO RELOADREGISTRY
//        ReloadListenerRegistry.register(PackType.SERVER_DATA, BlasterContent.BLASTER_DATA, Constants.withId("blaster_data"));

//        EnvExecutor.runInEnv(Env.CLIENT, () -> StarWarsClient::init); TODO SERVER/CLIENT RUNNER
//        LifecycleEvent.SETUP.register(StarWarsServer::init);
    }
}
