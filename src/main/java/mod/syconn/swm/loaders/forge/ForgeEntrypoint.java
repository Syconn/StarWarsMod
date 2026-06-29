//? if !fabric && !neoforge {
/*package mod.syconn.swm.loaders.forge;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.utils.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD)
public class ForgeEntrypoint {

    public ForgeEntrypoint(FMLJavaModLoadingContext context) {
        StarWars.initialize();
        context.getModEventBus().addListener(this::onRegister);
    }

    public void onRegister(RegisterEvent event) {
        Registrar.get(event.getRegistryKey()).forEach(entry -> entry.register(event::register));
    }
}
*///?}