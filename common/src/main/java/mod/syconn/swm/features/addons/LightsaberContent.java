package mod.syconn.swm.features.addons;

import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.json.JsonResourceReloader;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LightsaberContent {

    public static final JsonResourceReloader<LightsaberData> LIGHTSABER_DATA = new JsonResourceReloader<>(
            Constants.withId("lightsaber_defaults"), "lightsaber/defaults", LightsaberData::fromJson, LightsaberData::readTag);

    public static List<ItemStack> getLightsabers() {
        var list = new ArrayList<ItemStack>();
        LIGHTSABER_DATA.sets().forEach(entry -> list.add(entry.getValue().toItem(entry.getKey().getPath())));
        return list;
    }

    public static ItemStack createDefinedSaber(LightsaberTag tag) {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return tag.change(stack);
    }
}
