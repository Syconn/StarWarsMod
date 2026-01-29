package mod.syconn.swm.features.addons;

import mod.syconn.swm.features.blaster.server.data.BlasterJson;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.ColorUtil;
import mod.syconn.swm.utils.server.JsonResourceReloader;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlasterContent {

    public static final int RED = ColorUtil.packHsv(0f, 0.85f, 0.5f);
    public static final int GREEN = ColorUtil.packHsv(0.36f, 1f, 0.5f);
    public static final int BLUE = ColorUtil.packHsv(0.6f, 0.85f, 0.5f);

    public static final String BOLT = "bolt";
    public static final String STUN = "stun";

    public static final JsonResourceReloader<BlasterJson> BLASTER_DATA =
            new JsonResourceReloader<>(Constants.withId("blasters"), "blaster/defaults", BlasterJson::fromJson, BlasterJson::readTag, "models/item/blaster");

    public static List<ItemStack> getBlasters() {
        var list = new ArrayList<ItemStack>();
        BLASTER_DATA.sets().forEach(entry -> list.add(entry.getValue().toItem()));
        return list;
    }
}
