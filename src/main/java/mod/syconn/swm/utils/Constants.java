package mod.syconn.swm.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Constants {

    public static final String MOD = "swm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation withId(String path) {
        //? if 1.21.1
        //return ResourceLocation.fromNamespaceAndPath(MOD, path);
        //? if 1.20.1
        return new ResourceLocation(MOD, path);
    }
}
