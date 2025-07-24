package mod.syconn.swm.core;

import mod.syconn.swm.utils.Constants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModKeys {

    public static List<KeyMapping> KEYS = new ArrayList<>();

    public static final KeyMapping TOGGLE_ITEM = registerKeybind(keyId("toggle_item"), GLFW.GLFW_KEY_V, modCategory());
    public static final KeyMapping QUICK_SWAP_LIGHTSABER = registerKeybind(keyId("quick_swap_lightsaber"), GLFW.GLFW_KEY_K, modCategory());
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
