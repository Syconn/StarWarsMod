package mod.syconn.swm.utils.config;

import net.fabricmc.api.EnvType;

public enum ConfigType {

    CLIENT,
    SERVER,
    COMMON;


    public boolean shouldLoad(EnvType env) {
        return this == CLIENT && env == EnvType.CLIENT || (this == COMMON || this == SERVER) && env == EnvType.SERVER;
    }
}
