package mod.syconn.swm.features.lightsaber.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.util.json.JsonUtils;
import mod.syconn.swm.util.nbt.ISerializable;
import mod.syconn.swm.util.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public record LightsaberData(int model, boolean stable, float lengthScalar, double radius, int color, List<Vec3> emitterPositions) implements ISerializable<CompoundTag> {

    public LightsaberTag toTag() {
        return new LightsaberTag(UUID.randomUUID(), model, stable, lengthScalar, true, (byte) 0, radius, color, emitterPositions);
    }

    public ItemStack toItem() {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return toTag().change(stack);
    }

    public ItemStack toItem(String name) {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        stack.setHoverName(Component.literal(name.substring(0, 1).toUpperCase() + name.substring(1) + " Lightsaber"));
        return toTag().change(stack);
    }

    public static LightsaberData fromJson(JsonObject json) {
        return new LightsaberData(json.get("model").getAsInt(), json.get("stable").getAsBoolean(), json.get("length").getAsFloat(), json.get("radius").getAsDouble(), json.get("color").getAsInt(),
                JsonUtils.getArray(json.getAsJsonObject("emitterPositions"), JsonUtils::getVec3));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", model);
        json.addProperty("stable", stable);
        json.addProperty("length", lengthScalar);
        json.addProperty("radius", radius);
        json.addProperty("color", color);
        json.add("emitterPositions", JsonUtils.addArray(emitterPositions, JsonUtils::addVec3));
        return json;
    }

    public static LightsaberData readTag(CompoundTag tag) {
        return new LightsaberData(tag.getInt("model"), tag.getBoolean("stable"), tag.getFloat("lengthScalar"), tag.getDouble("radius"), tag.getInt("color"),
                NbtTools.getArray(tag.getCompound("vectors"), NbtTools::getVec3));
    }

    public CompoundTag writeTag() {
        var tag = new CompoundTag();
        tag.putInt("model", this.model);
        tag.putBoolean("stable", this.stable);
        tag.putFloat("lengthScalar", this.lengthScalar);
        tag.putDouble("radius", this.radius);
        tag.putInt("color", this.color);
        tag.put("vectors", NbtTools.putArray(this.emitterPositions, NbtTools::putVec3));
        return tag;
    }
}
