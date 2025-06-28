package mod.syconn.swm.neoforge;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.util.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD)
public final class StarWarsForge {

    public StarWarsForge() {
        StarWars.init();
    }
}
