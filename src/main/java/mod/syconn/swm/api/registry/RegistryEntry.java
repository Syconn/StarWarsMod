package mod.syconn.swm.api.registry;

import mod.syconn.swm.api.services.Registration;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryEntry<T> {

    protected final ResourceKey<Registry<T>> registry;
    protected final ResourceLocation id;
    protected final Supplier<T> supplier;

    public RegistryEntry(Registry<?> registry, ResourceLocation id, Supplier<T> supplier) {
        this.registry = ResourceKey.createRegistryKey(registry.key().location());
        this.id = id;
        this.supplier = supplier;
        this.tabbed = true;
    }

    protected boolean tabbed;
    private T instance;

    public T get() {
        if(this.instance == null) throw new IllegalStateException("Entry has not been created yet");
        return this.instance;
    }

    protected T create() {
        if(this.instance != null) throw new IllegalStateException("Entry has already been created");
        this.instance = this.supplier.get();
        return this.instance;
    }

    public ResourceKey<Registry<T>> getRegistry() {
        return registry;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    protected void invalidate() {
        this.instance = null;
    }

    public RegistryEntry<T> setTabbed(boolean tabbed) {
        this.tabbed = tabbed;
        return this;
    }

    public void register(RegisterConsumer<T> consumer) {
        this.invalidate();
        T value = this.create();
        consumer.accept(this.registry, this.id, () -> value);
    }

    public static <T extends Item> RegistryEntry<T> item(String id, Function<Item.Properties, T> itemFactory) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), () -> {
            var itemProperties = new Item.Properties();
            //? >1.21.11
            //itemProperties.setId(ResourceKey.create(Registries.ITEM, Constants.withId(id)));
            return itemFactory.apply(itemProperties);
        });
    }

    public static <T extends Item> RegistryEntry<T> item(String id, Function<Item.Properties, T> itemFactory, Supplier<Item.Properties> itemPropertiesFactory) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), () -> {
            var itemProperties = itemPropertiesFactory.get();
            //? >1.21.11
            //itemProperties.setId(ResourceKey.create(Registries.ITEM, Constants.withId(id)));
            return itemFactory.apply(itemProperties);
        });
    }

    public static RegistryEntry<Item> item(String id, Item.Properties properties) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), () -> {
            //? >1.21.11
            //properties.setId(ResourceKey.create(Registries.ITEM, Constants.withId(id)));
            return new Item(properties);
        });
    }

    public static RegistryEntry<CreativeModeTab> creativeModeTab(String id, Consumer<CreativeModeTab.Builder> builderConsumer) {
        return new RegistryEntry<>(BuiltInRegistries.CREATIVE_MODE_TAB, Constants.withId(id), () -> {
            var builder = Registration.createCreativeModeTabBuilder();
            builderConsumer.accept(builder);
            return builder.build();
        });
    }
}
