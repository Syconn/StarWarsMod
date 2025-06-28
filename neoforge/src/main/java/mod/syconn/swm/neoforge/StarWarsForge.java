package mod.syconn.swm.neoforge;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.util.Constants;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

@Mod(Constants.MOD)
public final class StarWarsForge {

    public StarWarsForge() {
        StarWars.init();
    }
}
