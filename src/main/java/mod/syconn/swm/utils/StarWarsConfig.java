package mod.syconn.swm.utils;

import me.fzzyhmstrs.fzzy_config.annotations.NonSync;
import me.fzzyhmstrs.fzzy_config.annotations.WithPerms;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.screen.context.ContextInput;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedKeybind;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import org.lwjgl.glfw.GLFW;

public class StarWarsConfig extends Config {

    public StarWarsConfig() {
        super(Constants.withId("config"));
    }

    @WithPerms
    public Server serverSettings = new Server();

    @NonSync
    public Client clientSettings = new Client();

    public static class Server extends ConfigSection {
        public ValidatedFloat swungLightsaberDamage = new ValidatedFloat(8.0f, 10000f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat thrownLightsaberDamage = new ValidatedFloat(10.0f, 10000f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat extraBlasterDamage = new ValidatedFloat(0.0f, 10000f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedBoolean allowBlasterDamageNonLiving = new ValidatedBoolean(false);
    }

    public static class Client extends ConfigSection {
        public ValidatedKeybind togglePrimaryBladeOnly = new ValidatedKeybind(GLFW.GLFW_KEY_N, ContextInput.KEYBOARD);
        public ValidatedKeybind toggleAllBlades = new ValidatedKeybind(GLFW.GLFW_KEY_V, ContextInput.KEYBOARD);
        public ValidatedKeybind quickSwapLightsaber = new ValidatedKeybind(GLFW.GLFW_KEY_K, ContextInput.KEYBOARD);
        public ValidatedKeybind throwLightsaber = new ValidatedKeybind(GLFW.GLFW_KEY_X, ContextInput.KEYBOARD);
        public ValidatedKeybind openSettings = new ValidatedKeybind(GLFW.GLFW_KEY_Y, ContextInput.KEYBOARD);
        public ValidatedBoolean toggleAds = new ValidatedBoolean(false);
    }
}
