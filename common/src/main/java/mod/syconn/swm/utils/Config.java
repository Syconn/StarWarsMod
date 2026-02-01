package mod.syconn.swm.utils;

import mod.syconn.swm.utils.config.ConfigType;
import mod.syconn.swm.utils.config.ConfigValue;
import mod.syconn.swm.utils.config.Configuration;

public class Config {

    @Configuration(id = Constants.MOD, name = "preferences", type = ConfigType.CLIENT)
    public static final Client CLIENT = new Client();

    public static class Client {

        @ConfigValue()
        public String testVal = "testing";

        @ConfigValue()
        public float test2 = 6f;
    }
}
