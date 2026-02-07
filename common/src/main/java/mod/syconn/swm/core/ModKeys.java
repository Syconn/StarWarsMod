package mod.syconn.swm.core;

import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.config.ConfigKey;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModKeys {

    public static List<KeyMapping> KEYS = new ArrayList<>();

    @ConfigKey(tooltip = "Toggles Main Blade")
    public static final KeyMapping TOGGLE_PRIMARY_BLADE = registerKeybind(keyId("toggle_primary_blade"), GLFW.GLFW_KEY_N, modCategory());

    @ConfigKey(tooltip = "Toggles all blades")
    public static final KeyMapping TOGGLE_BLADE = registerKeybind(keyId("toggle_blade"), GLFW.GLFW_KEY_V, modCategory());

    @ConfigKey(tooltip = "Grabs lightsaber from side")
    public static final KeyMapping QUICK_SWAP_LIGHTSABER = registerKeybind(keyId("quick_swap_lightsaber"), GLFW.GLFW_KEY_K, modCategory());

    @ConfigKey
    public static final KeyMapping THROW_LIGHTSABER = registerKeybind(keyId("throw_lightsaber"), GLFW.GLFW_KEY_X, modCategory());

    public static String modCategory() {
        return "key.categories." + Constants.MOD;
    }

    private static String keyId(String id) {
        return "key." + Constants.MOD + "." + id;
    }

    private static KeyMapping registerKeybind(String name, int keyCode, String category) {
        var key = new KeyMapping(name, keyCode, category);
        KEYS.add(key);
        return key;
    }
}
