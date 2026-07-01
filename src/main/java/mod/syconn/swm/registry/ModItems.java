package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static mod.syconn.swm.utils.Constants.MOD;

@AutoRegister
public class ModItems {

    public static final RegistryEntry<Item> LIGHTSABER = RegistryEntry.item("lightsaber", LightsaberItem::new);
    public static final RegistryEntry<Item> BLASTER = RegistryEntry.item("blaster", BlasterItem::new);
    public static final RegistryEntry<Item> DRILL = RegistryEntry.item("drill", new Item.Properties().stacksTo(1));
    public static final RegistryEntry<Item> MONITOR = RegistryEntry.item("monitor", new Item.Properties().stacksTo(1));
    public static final RegistryEntry<Item> DRIVER = RegistryEntry.item("driver", new Item.Properties().stacksTo(1));
    public static final RegistryEntry<Item> SCREEN = RegistryEntry.item("screen", new Item.Properties().stacksTo(1));

    public static final RegistryEntry<CreativeModeTab> TAB = RegistryEntry.creativeModeTab("star_wars", b -> b.title(Component.translatable("itemGroup." + MOD + ".starwars")).icon(() -> new ItemStack(LIGHTSABER.get()))
            .displayItems((params, output) -> addExtras(output)));

    public static void addExtras(CreativeModeTab.Output pOutput) {
        pOutput.acceptAll(Registrar.getItems());
        pOutput.acceptAll(LightsaberContent.getLightsabers(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        pOutput.acceptAll(BlasterContent.getBlasters(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }
}
