package mod.syconn.swm.utils.generic;

import dev.architectury.platform.Platform;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.utils.Constants;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class FileUtil {

    public static void scanFilesInDirectory(String name, Function<ResourceLocation, ResourceLocation> pathModifier, List<ResourceLocation> output) {
        output.addAll(FileToIdConverter.json(name).listMatchingResources(GameInstance.getClient().getResourceManager()).keySet().stream().map(pathModifier).toList());
    }

    public static Path config(String id, String file) {
        Path configDir = Platform.getConfigFolder().resolve(id);
        Path configPath = configDir.resolve(file + ".toml");

        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config directory", e);
        }

        return configPath;
    }
}
