//? if fabric {
package mod.syconn.swm.loaders.fabric;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.api.registry.BlockRegistryEntry;
import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.api.util.Env;
import mod.syconn.swm.api.util.Environment;
import mod.syconn.swm.loaders.fabric.events.PlayerEvents;
import mod.syconn.swm.server.StarWarsServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;

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

        PlayerEvents.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);
        PlayerEvents.PLAYER_DISCONNECT.register(StarWarsServer::playerLeaveServer);
        ServerTickEvents.END_SERVER_TICK.register(StarWarsServer::serverTick);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> Environment.setExecutor(Env.SERVER, server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> Environment.setExecutor(Env.SERVER, null));
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
