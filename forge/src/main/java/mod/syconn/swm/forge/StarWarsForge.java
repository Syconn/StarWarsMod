package mod.syconn.swm.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.syconn.swm.StarWars;
import mod.syconn.swm.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD)
public final class StarWarsForge {

    public StarWarsForge() {
        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Constants.MOD, bus);

        StarWars.init();
    }
}
