package mod.syconn.swm.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@FunctionalInterface
public interface RegisterConsumer<T> {
    void accept(ResourceKey<Registry<T>> registryKey, ResourceLocation id, Supplier<T> valueSupplier);
}
