//? if fabric {
package mod.syconn.swm.loaders.fabric;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.api.registry.Registrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        StarWars.initialize();

        Registrar.getEntries().forEach(entry -> {
            entry.register((registryKey, path, valueSupplier) -> {
                @SuppressWarnings("unchecked")
                Registry<Object> registry = (Registry<Object>) BuiltInRegistries.REGISTRY.get(registryKey.location());
                if (registry == null) throw new IllegalStateException("Unknown registry: " + registryKey.location());
                Registry.register(registry, path, valueSupplier.get());
            });
        });
    }
}
//?}
