package mod.syconn.swm.api.services;

//? fabric {
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.SharedConstants;
//? }

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Platform {
    private static final Map<String, Mod> mods = new ConcurrentHashMap<>();

    public static Path getConfigFolder() {
        //? fabric
        return FabricLoader.getInstance().getConfigDir().toAbsolutePath().normalize();
    }

    public static Env getEnv() {
        //? fabric
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Env.CLIENT : Env.SERVER;
    }

    public static boolean isModLoaded(String id) {
        //? fabric
        return FabricLoader.getInstance().isModLoaded(id);
    }

    public static Mod getMod(String id) {
        return mods.computeIfAbsent(id, ModImpl::new);
    }

    public static boolean isDevelopmentEnvironment() {
        //? fabric
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }

    private static class ModImpl implements Mod {
        private final ModMetadata metadata;

        public ModImpl(String id) {
            ModContainer container = FabricLoader.getInstance().getModContainer(id).orElseThrow();
            this.metadata = container.getMetadata();
        }

        @Override
        public String getModId() {
            return metadata.getId();
        }

        @Override
        public String getVersion() {
            return metadata.getVersion().getFriendlyString();
        }

        @Override
        public String getName() {
            return metadata.getName();
        }

        @Override
        public String getDescription() {
            return metadata.getDescription();
        }
    }
}
