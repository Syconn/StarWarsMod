package mod.syconn.swm;

import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.network.Network;

public class StarWars {

    public static void initialize() {
        Registrar.loadRegistries();
        Network.init();

//        ReloadListenerRegistry.register(PackType.SERVER_DATA, LightsaberContent.LIGHTSABER_DATA, Constants.withId("lightsaber_data")); TODO RELOADREGISTRY
//        ReloadListenerRegistry.register(PackType.SERVER_DATA, BlasterContent.BLASTER_DATA, Constants.withId("blaster_data"));

//        LifecycleEvent.SETUP.register(StarWarsServer::init);
    }
}
