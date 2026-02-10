package mod.syconn.swm.utils;

import mod.syconn.swm.core.ModKeys;
import mod.syconn.swm.utils.config.ConfigType;
import mod.syconn.swm.utils.config.ConfigValue;
import mod.syconn.swm.utils.config.Configuration;

public class Config {

    @Configuration(id = Constants.MOD, name = "preferences", type = ConfigType.CLIENT, keyMappings = ModKeys.class)
    public static final Client CLIENT = new Client();

    @Configuration(id = Constants.MOD, name = "settings", type = ConfigType.SERVER)
    public static final Server SERVER = new Server();

    public static class Client {

        @ConfigValue(comment = "Blaster Settings")
        public boolean toggleAds = false;
    }

    public static class Server {

        @ConfigValue(comment = "Base Damages")
        public float baseLightsaberDamage = 8.0f;

        @ConfigValue
        public float baseBlasterDamage = 0.0f;

        @ConfigValue
        public boolean allowBlasterNonlivingDamage = false;
    }
}
