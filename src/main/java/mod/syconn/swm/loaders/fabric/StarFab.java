//? if fabric {
package mod.syconn.swm.loaders.fabric;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.api.registry.BlockRegistryEntry;
import mod.syconn.swm.api.registry.Registrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

public class StarFab implements ModInitializer {

    @Override
    public void onInitialize() {
        StarWars.initialize();

        Registrar.getEntries().forEach(entry -> {
            entry.register((registryKey, path, valueSupplier) -> {
                Registry<Object> registry = getRegistry(registryKey);
                if (registry == null) throw new IllegalStateException("Unknown registry: " + registryKey.location());
                Registry.register(registry, path, valueSupplier.get());
            });
        });

        Registrar.get(Registries.BLOCK).forEach(entry -> {
            if(entry instanceof BlockRegistryEntry<?, ?> blockEntry) blockEntry.item().ifPresent(item -> Registry.register(BuiltInRegistries.ITEM, entry.getId(), item));
        });
    }

    @SuppressWarnings("unchecked")
    private static Registry<Object> getRegistry(ResourceKey<? extends Registry<?>> key) {
        //? if <=1.21.1
        return (Registry<Object>) BuiltInRegistries.REGISTRY.get(key.location());
        //? if >=26.1.2
        //return (Registry<Object>) BuiltInRegistries.REGISTRY.getValue(key.location());
    }
}
//?}
