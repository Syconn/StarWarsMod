package mod.syconn.swm.features.lightsaber.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.utils.client.NodeVec3;
import mod.syconn.swm.utils.generic.JsonUtil;
import mod.syconn.swm.utils.generic.NBTUtil;
import mod.syconn.swm.utils.interfaces.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public record LightsaberJson(ResourceLocation model, int version, List<BladeData> blades) implements ISerializable<CompoundTag> {

    public LightsaberTag toTag() {
        return new LightsaberTag(UUID.randomUUID(), this.model.withPath("lightsaber/" + this.model.getPath()), this.version, this.blades);
    }

    public ItemStack toItem() {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return toTag().change(stack);
    }

    public static LightsaberJson fromJson(JsonObject json) {
        return new LightsaberJson(new ResourceLocation(json.get("model").getAsString()), json.get("version").getAsInt(), JsonUtil.getArray(json.get("blades").getAsJsonObject(), BladeData::new));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", this.model.toString());
        json.addProperty("version", this.version);
        json.add("blades", JsonUtil.putArray(this.blades, BladeData::json));
        return json;
    }

    public static LightsaberJson readTag(CompoundTag tag) {
        return new LightsaberJson(new ResourceLocation(tag.getString("model")), tag.getInt("version"), NBTUtil.getList(tag.getCompound("blades"), BladeData::new));
    }

    public CompoundTag writeTag() {
        var tag = new CompoundTag();
        tag.putString("model", this.model.toString());
        tag.putInt("version", this.version);
        tag.put("blades", NBTUtil.putList(this.blades, BladeData::save));
        return tag;
    }
}
