package mod.syconn.swm.fabric;

import mod.syconn.swm.StarWars;
import net.fabricmc.api.ModInitializer;

public final class StarWarsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StarWars.init();
    }
}
