package mod.syconn.swm.utils.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public class FileUtil {

    public static void scanFilesInDirectory(String name, Function<ResourceLocation, ResourceLocation> pathModifier, List<ResourceLocation> output) {
        output.addAll(FileToIdConverter.json(name).listMatchingResources(Minecraft.getInstance().getResourceManager()).keySet().stream().map(pathModifier).toList());
    }
}
