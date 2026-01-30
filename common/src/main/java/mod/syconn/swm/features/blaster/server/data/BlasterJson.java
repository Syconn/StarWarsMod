package mod.syconn.swm.features.blaster.server.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.utils.generic.NBTUtil;
import mod.syconn.swm.utils.interfaces.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record BlasterJson(ResourceLocation model, int version, MuzzleData muzzleData) implements ISerializable<CompoundTag> {

    public BlasterTag toTag() {
        return new BlasterTag(UUID.randomUUID(), this.model.withPath("blaster/" + this.model.getPath()), version, muzzleData);
    }

    public ItemStack toItem() {
        var stack = new ItemStack(ModItems.BLASTER.get());
        return toTag().change(stack);
    }

    public static BlasterJson fromJson(JsonObject json) {
        System.out.println(json);

        return new BlasterJson(new ResourceLocation(json.get("model").getAsString()), json.get("version").getAsInt(), new MuzzleData(json.get("muzzle").getAsJsonObject()));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", this.model.toString());
        json.addProperty("version", this.version);
        json.add("muzzle", this.muzzleData.json());
        return json;
    }

    public static BlasterJson readTag(CompoundTag tag) {
        return new BlasterJson(new ResourceLocation(tag.getString("model")), tag.getInt("version"), new MuzzleData(tag.getCompound("muzzle")));
    }

    @Override
    public CompoundTag writeTag() {
        var tag = new CompoundTag();
        tag.putString("model", this.model.toString());
        tag.putInt("version", this.version);
        tag.put("muzzle", this.muzzleData.save());
        return tag;
    }
}
