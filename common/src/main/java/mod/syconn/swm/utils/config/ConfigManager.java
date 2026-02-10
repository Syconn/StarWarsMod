package mod.syconn.swm.utils.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import dev.architectury.platform.Platform;
import mod.syconn.swm.utils.generic.FileUtil;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private static final List<Class<?>> CONFIGS = new ArrayList<>();
    public static final List<KeyMapping> MAPPINGS = new ArrayList<>();

    public static void load() {
        CONFIGS.forEach(ConfigManager::loadConfig);
    }

    private static void loadConfig(Class<?> configClass) {
        for (var field : configClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Configuration.class)) continue;

            var annotation = field.getAnnotation(Configuration.class);
            if (!annotation.type().shouldLoad(Platform.getEnv())) continue;
            if (annotation.keyMappings() != Void.class) registerKeyMappings(annotation.keyMappings());

            try {
                var path = FileUtil.config(annotation.id(), annotation.name() + "-" + annotation.type().toString().toLowerCase());
                var config = CommentedFileConfig.builder(path).autoreload().autosave().sync().build();
                config.load();
                loadConfigFields(config, field.get(null));
                config.close();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void loadConfigFields(CommentedFileConfig config, Object configInstance) {
        var configuration = configInstance.getClass();

        for (var configValue : configuration.getDeclaredFields()) {
            if (!configValue.isAnnotationPresent(ConfigValue.class)) continue;

            try {
                var comment = configValue.getAnnotation(ConfigValue.class).comment();
                var id = configValue.getName();

                if (!comment.isEmpty()) config.setComment(id, comment);
                if (!config.contains(id)) config.set(id, configValue.get(configInstance));
                else configValue.set(configInstance, safeArg(config.get(id), configValue.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Object safeArg(Object value, Class<?> targetType) {
        if (!(value instanceof Number number)) return value;

        if (targetType == int.class || targetType == Integer.class) return number.intValue();
        if (targetType == float.class || targetType == Float.class) return number.floatValue();
        if (targetType == double.class || targetType == Double.class) return number.doubleValue();
        if (targetType == long.class || targetType == Long.class) return number.longValue();
        if (targetType == short.class || targetType == Short.class) return number.shortValue();
        if (targetType == byte.class || targetType == Byte.class) return number.byteValue();

        return value;
    }

    private static void registerKeyMappings(Class<?> keyClass) {
        for (var keyField : keyClass.getDeclaredFields()) {
            if (!keyField.isAnnotationPresent(ConfigKey.class)) continue;

            try {
                if (keyField.get(null) instanceof KeyMapping mapping) MAPPINGS.add(mapping);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void register(Class<?> configClass) {
        CONFIGS.add(configClass);
    }

    public static void registerAndLoad(Class<?> configClass) {
        CONFIGS.add(configClass);
        load();
    }
}
