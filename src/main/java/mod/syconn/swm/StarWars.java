package mod.syconn.swm;

import mod.syconn.swm.api.registry.Registrar;

public class StarWars {

    public static void initialize() {
        Registrar.loadRegistries();
    }
}
