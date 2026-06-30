package mod.syconn.swm.utils.interfaces;

import mod.syconn.swm.utils.generic.StringUtil;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public interface ISpecialRenderer {

    List<ModelResourceLocation> SPECIAL_RENDERS = new ArrayList<>();
    List<String> SPECIAL_RENDER_FOLDER = new ArrayList<>();

    static void registerPath(String path) {
        SPECIAL_RENDER_FOLDER.add(path);
    }

    static void registerSpecial(ModelResourceLocation modelResourceLocation) {
        SPECIAL_RENDERS.add(modelResourceLocation);
    }

    static void registerSpecial(ResourceLocation resourceLocation) {
        SPECIAL_RENDERS.add(new ModelResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath(), "inventory"));
    }

    static ModelResourceLocation itemModelPath(ResourceLocation location) {
        return new ModelResourceLocation(location.getNamespace(), StringUtil.trimSuffix(StringUtil.trimPrefix(location.getPath(), "models/item/"), ".json"), "inventory");
    }
}
