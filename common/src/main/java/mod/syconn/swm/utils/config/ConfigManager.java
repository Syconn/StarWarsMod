package mod.syconn.swm.utils.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import dev.architectury.platform.Platform;
import mod.syconn.swm.utils.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ConfigManager {

    private final CommentedFileConfig config;

    public ConfigManager() {
        Path configDir = Platform.getConfigFolder().resolve(Constants.MOD);
        Path configPath = configDir.resolve("preferences.toml");

        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config directory", e);
        }

        config = CommentedFileConfig.builder(configPath).autoreload().autosave().sync().build();
        config.load();
//        featureValue = config.getOrElse("featureValue", 5);
    }

    public void dynamicSave(Consumer<CommentedFileConfig> configurator) {
        configurator.accept(this.config);
        this.config.save();
    }

    public CommentedFileConfig getConfig() {
        return this.config;
    }
}
