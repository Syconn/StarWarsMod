package mod.syconn.swm.features.addons;

import mod.syconn.swm.features.lightsaber.data.LightsaberJson;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.ColorUtil;
import mod.syconn.swm.utils.server.JsonResourceReloader;
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

    public static final JsonResourceReloader<LightsaberJson> LIGHTSABER_DATA =
            new JsonResourceReloader<>(Constants.withId("lightsaber_defaults"), "lightsaber/defaults", LightsaberJson::fromJson, LightsaberJson::readTag, "models/item/lightsaber");

    public static List<ItemStack> getLightsabers() {
        var list = new ArrayList<ItemStack>();
        LIGHTSABER_DATA.sets().forEach(entry -> list.add(entry.getValue().toItem()));
        return list;
    }
}
