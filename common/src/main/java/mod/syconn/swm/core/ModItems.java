package mod.syconn.swm.core;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.swm.util.Constants.MOD;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<Item> LIGHTSABER = registerItem("lightsaber", LightsaberItem::new);
    public static final RegistrySupplier<Item> DRILL = registerItem("drill", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> MONITOR = registerItem("monitor", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> DRIVER = registerItem("driver", new Item.Properties().stacksTo(1));
    public static final RegistrySupplier<Item> SCREEN = registerItem("screen", new Item.Properties().stacksTo(1));

    public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register("star_wars", () -> CreativeTabRegistry.create(builder ->
            builder.title(Component.translatable("itemGroup." + MOD + ".star_wars")).icon(() -> LightsaberContent.getLightsabers().get(0))
                    .displayItems(ModItems::addCreative).build()));

    public static void addCreative(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
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
