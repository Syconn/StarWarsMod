package mod.syconn.swm;

import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.api.registry.server.ReloadRegistry;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.network.Network;
import net.minecraft.server.packs.PackType;

public class StarWars {

    public static void initialize() {
        Registrar.loadRegistries();
        Network.init();

        ReloadRegistry.register(PackType.SERVER_DATA, LightsaberContent.LIGHTSABER_DATA, "lightsaber_data");
        ReloadRegistry.register(PackType.SERVER_DATA, BlasterContent.BLASTER_DATA, "blaster_data");
    }

    public static void registerReloads(ReloadRegistry registry) {

    }
}
