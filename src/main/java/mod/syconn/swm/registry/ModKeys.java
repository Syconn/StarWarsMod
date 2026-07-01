package mod.syconn.swm.registry;

import mod.syconn.swm.api.util.ConfigKeybind;
import mod.syconn.swm.utils.Constants;

public class ModKeys {

    public static ConfigKeybind TOGGLE_BLADE = new ConfigKeybind(Constants.CONFIG.clientSettings.toggleAllBlades);
    public static ConfigKeybind TOGGLE_PRIMARY_BLADE = new ConfigKeybind(Constants.CONFIG.clientSettings.togglePrimaryBladeOnly);
    public static ConfigKeybind THROW_LIGHTSABER = new ConfigKeybind(Constants.CONFIG.clientSettings.throwLightsaber);
    public static ConfigKeybind QUICK_SWAP_LIGHTSABER = new ConfigKeybind(Constants.CONFIG.clientSettings.quickSwapLightsaber);
}
