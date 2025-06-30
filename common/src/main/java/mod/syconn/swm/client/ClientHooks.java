package mod.syconn.swm.client;

import mod.syconn.swm.client.screen.HologramScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public class ClientHooks {

    public static Screen createHologramScreen() {
        return new HologramScreen();
    }
}
