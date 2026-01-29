package mod.syconn.swm.features.blaster.server.data;

import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.lightsaber.server.data.BladeData;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

public class BlasterTag {

    private static final String ID = "blaster";

    public UUID uuid;
    public ResourceLocation model;
    public int version;
    public MuzzleData muzzle;

    public BlasterTag(UUID uuid, ResourceLocation model, int version, MuzzleData muzzleData) {
        this.uuid = uuid;
        this.model = model;
        this.version = version;
        this.muzzle = muzzleData;
    }

    public BlasterTag(CompoundTag tag) {
        this.uuid = tag.contains("uuid") ? tag.getUUID("uuid") : UUID.randomUUID();
        this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("blaster/f11");
        this.muzzle = new MuzzleData(tag.getCompound("muzzle"));
//        updateData(tag.getInt("version"));
    }

//    private void updateData(int value) { TODO WONT WORK SOME REASN
//        this.version = value;
//        var saved = BlasterContent.BLASTER_DATA.get(this.model);
//        if (saved == null) Constants.LOG.warn("Invalid Blaster Tag for {}", this.model);
//        else if (this.version != saved.version()) {
//            var tag = saved.toTag().save();
//            this.uuid = tag.contains("uuid") ? tag.getUUID("uuid") : UUID.randomUUID();
//            this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("f11");
//            this.muzzle = new MuzzleData(tag.getCompound("muzzle"));
//            this.version = saved.version();
//        }
//    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putUUID("uuid", this.uuid);
        tag.putString("model", this.model.toString());
        tag.putInt("version", this.version);
        tag.put("muzzle", this.muzzle.save());
        return tag;
    }

    public static BlasterTag getOrCreate(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ID)) return create(stack);
        return new BlasterTag(stack.getOrCreateTag().getCompound(ID));
    }

    public static ItemStack update(ItemStack stack, Consumer<BlasterTag> consumer) {
        var lT = getOrCreate(stack);
        consumer.accept(lT);
        return lT.change(stack);
    }

    private static BlasterTag create(ItemStack stack) {
        var lT = BlasterContent.BLASTER_DATA.get(Constants.withId("blaster/f11")).toTag();
        lT.change(stack);
        return lT;
    }
}
