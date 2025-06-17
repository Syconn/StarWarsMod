package mod.syconn.swm.features.lightsaber.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.util.json.JsonUtils;
import mod.syconn.swm.util.nbt.ISerializable;
import mod.syconn.swm.util.nbt.NbtTools;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public record LightsaberData(int model, boolean stable, double radius, int color, List<Vec3> emitterPositions) implements ISerializable<CompoundTag> {

    public LightsaberComponent component() {
        return new LightsaberComponent(model, stable, true, (byte) 0, 1, radius, color, emitterPositions);
    }

    public ItemStack toItem(String name) {
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name.substring(0, 1).toUpperCase() + name.substring(1) + " Lightsaber"));
        return LightsaberComponent.set(stack, component());
    }

    public static LightsaberData fromJson(JsonObject json) {
        return new LightsaberData(json.get("model").getAsInt(), json.get("stable").getAsBoolean(), json.get("radius").getAsDouble(), json.get("color").getAsInt(),
                JsonUtils.getArray(json.getAsJsonObject("emitterPositions"), JsonUtils::getVec3));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", model);
        json.addProperty("stable", stable);
        json.addProperty("radius", radius);
        json.addProperty("color", color);
        json.add("emitterPositions", JsonUtils.addArray(emitterPositions, JsonUtils::addVec3));
        return json;
    }

    public static LightsaberData readTag(CompoundTag tag) {
        return new LightsaberData(tag.getInt("model"), tag.getBoolean("stable"), tag.getDouble("radius"), tag.getInt("color"),
                NbtTools.getArray(tag.getCompound("vectors"), NbtTools::getVec3));
    }

    public CompoundTag writeTag() {
        var tag = new CompoundTag();
        tag.putInt("model", this.model);
        tag.putBoolean("stable", this.stable);
        tag.putDouble("radius", this.radius);
        tag.putInt("color", this.color);
        tag.put("vectors", NbtTools.putArray(this.emitterPositions, NbtTools::putVec3));
        return tag;
    }
}
