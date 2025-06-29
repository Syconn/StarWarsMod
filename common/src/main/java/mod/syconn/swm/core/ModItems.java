package mod.syconn.swm.core;

import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swm.util.Constants.MOD;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<Item> LIGHTSABER = registerItem("lightsaber", LightsaberItem::new);
    public static final RegistrySupplier<Item> F11 = registerItem("f_11", BlasterItem::new);
    public static final RegistrySupplier<Item> DRILL = registerItem("drill", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> MONITOR = registerItem("monitor", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> DRIVER = registerItem("driver", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> SCREEN = registerItem("screen", new Item.Properties().stacksTo(1));

    public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register("star_wars", () -> CreativeTabRegistry.create(
            Component.translatable("itemGroup." + MOD + ".star_wars"), () -> new ItemStack(LIGHTSABER.get())));

    public static void addCreative(FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks) {
        output.acceptAll(LightsaberContent.getLightsabers());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Item.Properties properties) {
        return (RegistrySupplier<T>) registerItem(id, Item::new, properties);
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Supplier<T> factory) {
        return ITEMS.register(id, factory);
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties properties) {
        return ITEMS.register(id, () -> factory.apply(properties.arch$tab(TAB)));
    }
}
