package mod.syconn.swm.features.lightsaber.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.util.client.model.NodeVec3;
import mod.syconn.swm.util.json.JsonUtils;
import mod.syconn.swm.util.nbt.ISerializable;
import mod.syconn.swm.util.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public record LightsaberData(int model, boolean stable, float lengthScalar, double radius, int color, String bladeType, List<NodeVec3> emitterPositions) implements ISerializable<CompoundTag> {

    public LightsaberTag toTag() {
        return new LightsaberTag(UUID.randomUUID(), this.model, this.stable, this.lengthScalar, true, (byte) 0, this.radius, this.color, this.bladeType, this.emitterPositions);
    }

    public ItemStack toItem() {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return toTag().change(stack);
    }

    public ItemStack toItem(String name) {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        stack.setHoverName(Component.literal(name + " Lightsaber"));
        return toTag().change(stack);
    }

    public static LightsaberData fromJson(JsonObject json) {
        return new LightsaberData(json.get("model").getAsInt(), json.get("stable").getAsBoolean(), json.get("length").getAsFloat(), json.get("radius").getAsDouble(), json.get("color").getAsInt(),
                json.get("bladeType").getAsString(), JsonUtils.getArray(json.getAsJsonObject("vectors"), NodeVec3::getNode));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", this.model);
        json.addProperty("stable", this.stable);
        json.addProperty("length", this.lengthScalar);
        json.addProperty("radius", this.radius);
        json.addProperty("color", this.color);
        json.addProperty("bladeType", this.bladeType);
        json.add("vectors", JsonUtils.addArray(this.emitterPositions, NodeVec3::addNode));
        return json;
    }

    public static LightsaberData readTag(CompoundTag tag) {
        return new LightsaberData(tag.getInt("model"), tag.getBoolean("stable"), tag.getFloat("lengthScalar"), tag.getDouble("radius"), tag.getInt("color"),
                tag.getString("bladeType"), NbtTools.getArray(tag.getCompound("vectors"), NodeVec3::getNode));
    }

    public CompoundTag writeTag() {
        var tag = new CompoundTag();
        tag.putInt("model", this.model);
        tag.putBoolean("stable", this.stable);
        tag.putFloat("lengthScalar", this.lengthScalar);
        tag.putDouble("radius", this.radius);
        tag.putInt("color", this.color);
        tag.putString("bladeType", this.bladeType);
        tag.put("vectors", NbtTools.putArray(this.emitterPositions, NodeVec3::putNode));
        return tag;
    }
}
