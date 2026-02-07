package mod.syconn.swm.utils;

import mod.syconn.swm.core.ModKeys;
import mod.syconn.swm.utils.config.ConfigType;
import mod.syconn.swm.utils.config.ConfigValue;
import mod.syconn.swm.utils.config.Configuration;

public class Config {

    @Configuration(id = Constants.MOD, name = "preferences", type = ConfigType.CLIENT, keyMappings = ModKeys.class)
    public static final Client CLIENT = new Client();

    public static class Client {

    }
}
