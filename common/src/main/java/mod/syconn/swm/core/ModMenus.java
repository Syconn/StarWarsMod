package mod.syconn.swm.core;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.features.lightsaber.client.screen.LightsaberAssemblerScreen;
import mod.syconn.swm.features.lightsaber.client.screen.LightsaberWorkbenchScreen;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberAssemblerMenu;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberWorkbenchMenu;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Constants.MOD, Registry.MENU_REGISTRY);

    public static final RegistrySupplier<MenuType<LightsaberWorkbenchMenu>> LIGHTSABER_WORKBENCH = MENUS.register("lightsaber_workbench", () -> MenuRegistry.ofExtended(LightsaberWorkbenchMenu::new));
    public static final RegistrySupplier<MenuType<LightsaberAssemblerMenu>> LIGHTSABER_ASSEMBLER = MENUS.register("lightsaber_assembler", () -> MenuRegistry.ofExtended(LightsaberAssemblerMenu::new));

    public static void registerScreens() {
        MenuRegistry.registerScreenFactory(LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchScreen::new);
        MenuRegistry.registerScreenFactory(LIGHTSABER_ASSEMBLER.get(), LightsaberAssemblerScreen::new);
    }
}
