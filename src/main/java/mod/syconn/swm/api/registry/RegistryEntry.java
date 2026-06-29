package mod.syconn.swm.api.registry;

import mod.syconn.swm.utils.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class RegistryEntry<T> {

    protected final ResourceKey<Registry<T>> registry;
    protected final ResourceLocation id;
    protected final Supplier<T> supplier;

    public RegistryEntry(Registry<?> registry, ResourceLocation id, Supplier<T> supplier) {
        this.registry = ResourceKey.createRegistryKey(registry.key().location());
        this.id = id;
        this.supplier = supplier;
    }

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

    public void register(RegisterConsumer<T> consumer) {
        this.invalidate();
        T value = this.create();
        consumer.accept(this.registry, this.id, () -> value);
    }

    public static <T extends Item> RegistryEntry<T> item(String id, Supplier<T> supplier) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), supplier);
    }
}
