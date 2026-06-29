package mod.syconn.swm.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Constants {

    public static final String MOD = "swm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation withId(String path) {
        return new ResourceLocation(MOD, path);
    }
}
