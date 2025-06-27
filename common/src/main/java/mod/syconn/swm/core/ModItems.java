package mod.syconn.swm.core;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swm.util.Constants.MOD;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD, Registry.ITEM_REGISTRY);

    public static final List<RegistrySupplier<Item>> DEFAULT_ITEMS = new ArrayList<>();

    public static final RegistrySupplier<Item> LIGHTSABER = registerItem("lightsaber", LightsaberItem::new);
    public static final RegistrySupplier<Item> DRILL = registerItem("drill", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> MONITOR = registerItem("monitor", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> DRIVER = registerItem("driver", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> SCREEN = registerItem("screen", new Item.Properties().stacksTo(1));

    public static final CreativeModeTab TAB = CreativeTabRegistry.create(Constants.withId("star_wars"), () -> new ItemStack(LIGHTSABER.get()));

    @SuppressWarnings("unchecked")
    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Item.Properties properties) {
        RegistrySupplier<Item> item = registerItem(id, Item::new, properties);
        DEFAULT_ITEMS.add(item);
        return (RegistrySupplier<T>)  item;
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Supplier<T> factory) {
        return ITEMS.register(id, factory);
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties properties) {
        return ITEMS.register(id, () -> factory.apply(properties.tab(TAB)));
    }
}
