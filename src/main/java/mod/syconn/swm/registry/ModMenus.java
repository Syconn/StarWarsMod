package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.features.lightsaber.server.menu.LightsaberAssemblerMenu;
import mod.syconn.swm.features.lightsaber.server.menu.LightsaberWorkbenchMenu;
import net.minecraft.world.inventory.MenuType;

@AutoRegister
public class ModMenus {

    public static final RegistryEntry<MenuType<LightsaberWorkbenchMenu>> LIGHTSABER_WORKBENCH = RegistryEntry.menuTypeWithData("lightsaber_workbench", LightsaberWorkbenchMenu::new);
    public static final RegistryEntry<MenuType<LightsaberAssemblerMenu>> LIGHTSABER_ASSEMBLER = RegistryEntry.menuTypeWithData("lightsaber_assembler", LightsaberAssemblerMenu::new);
}
