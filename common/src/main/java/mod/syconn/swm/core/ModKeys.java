package mod.syconn.swm.core;

import mod.syconn.swm.utils.Constants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeys {

    public static final KeyMapping TOGGLE_ITEM = new KeyMapping(keyId("toggle_item"), GLFW.GLFW_KEY_V, modCategory());
    public static final KeyMapping POWER_1 = new KeyMapping(keyId("power_1"), GLFW.GLFW_KEY_X, modCategory());

    public static String modCategory() {
        return "key.categories." + Constants.MOD;
    }

    private static String keyId(String id) {
        return "key." + Constants.MOD + "." + id;
    }
}
