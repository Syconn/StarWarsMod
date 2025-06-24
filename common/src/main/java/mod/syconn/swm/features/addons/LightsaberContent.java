package mod.syconn.swm.features.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.StringUtil;
import mod.syconn.swm.util.json.JsonResourceReloader;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LightsaberContent {

    public static final int GREEN = ColorUtil.packHsv(0.36f, 1f, 0.5f);
    public static final int BLUE = ColorUtil.packHsv(0.6f, 0.85f, 0.5f);
    public static final int PURPLE = ColorUtil.packHsv(0.8f, 1f, 0.5f);
    public static final int YELLOW = ColorUtil.packHsv(0.17f, 0.85f, 0.5f);
    public static final int RED = ColorUtil.packHsv(0f, 0.85f, 0.5f);
    public static final int WHITE = ColorUtil.packHsv(0f, 0f, 0.85f);

    public static final String PLASMA = "plasma";
    public static final String DARK_SABER = "dark_saber";
    public static final String BRICK = "brick";

    private static final int MODEL_FIXER = 6;

    public static final JsonResourceReloader<LightsaberData> LIGHTSABER_DATA = new JsonResourceReloader<>(Constants.withId("lightsaber_defaults"), "lightsaber/defaults",
            LightsaberData::fromJson, LightsaberData::readTag);

    public static List<ItemStack> getLightsabers() {
        var list = new ArrayList<ItemStack>();
        LIGHTSABER_DATA.sets().forEach(entry -> list.add(entry.getValue().toItem(StringUtil.makeLightsaberName(entry.getKey().getPath()))));
        return list;
    }

    public static ItemStack createDefinedSaber(LightsaberTag tag) {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return tag.change(stack);
    }

    public static void renderFixes(ItemDisplayContext renderMode, PoseStack poseStack, ItemStack stack) {
        if (stack.getItem() instanceof LightsaberItem) {
            var lt = LightsaberTag.getOrCreate(stack);
            if (lt.model == MODEL_FIXER) {
                if (renderMode == ItemDisplayContext.NONE) poseStack.scale(0.5f, 0.5f, 0.5f);
                else poseStack.scale(3f, 1.5f, 3f);
            }
        }
    }
}
