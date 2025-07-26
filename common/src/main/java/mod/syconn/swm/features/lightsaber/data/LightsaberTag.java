package mod.syconn.swm.features.lightsaber.data;

import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class LightsaberTag { // TODO LOCKED = UPDATE FROM LATEST DATA (DEFAULT T), SECONDARY_STATE VALID BLADES ID"S TO RENDER
    // TODO TIMED ANIMATION SO each blade is different time

    private static final String ID = "lightsaber";

    public UUID uuid;
    public ResourceLocation model;
    public int version;
    public List<BladeData> blades;

    public LightsaberTag(UUID uuid, ResourceLocation model, int version, List<BladeData> blades) {
        this.uuid = uuid;
        this.model = model;
        this.version = version;
        this.blades = blades;
    }

    public LightsaberTag(CompoundTag tag) {
        this.uuid = tag.contains("uuid") ? tag.getUUID("uuid") : UUID.randomUUID();
        this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("yoda");
        this.blades = NBTUtil.getList(tag.getCompound("blades"), BladeData::new);
        updateData(tag.getInt("version"));
    }

    private void updateData(int value) {
        this.version = value;
        var saved = LightsaberContent.LIGHTSABER_DATA.get(this.model);
        if (this.version != saved.version()) {
            var tag = saved.toTag().save();
            this.uuid = tag.contains("uuid") ? tag.getUUID("uuid") : UUID.randomUUID();
            this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("yoda");
            this.blades = NBTUtil.getList(tag.getCompound("blades"), BladeData::new);
            this.version = saved.version();
        }
    }

    public ItemStack getTemporary(boolean active, boolean singleBlade) {
        if (singleBlade && !this.blades.isEmpty()) this.blades.get(0).active = active;
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return change(stack);
    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public void toggleAll() {
        for (var blade : this.blades) blade.toggle();
    }

    public void tick() {
        for (var blade : this.blades) blade.tick();
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putUUID("uuid", this.uuid);
        tag.putString("model", this.model.toString());
        tag.putInt("version", this.version);
        tag.put("blades", NBTUtil.putList(this.blades, BladeData::save));
        return tag;
    }

    public static ItemStack getTemporary(ItemStack stack, boolean active) {
        return getTemporary(getOrCreate(stack), active);
    }

    public static ItemStack getTemporary(LightsaberTag original, boolean active) {
        original.blades.forEach(b -> b.active = active);
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return original.change(stack);
    }

    public static boolean identical(ItemStack stack1, ItemStack stack2) {
        if (!(stack1.getItem() instanceof LightsaberItem && stack1.getItem() == stack2.getItem())) return false;
        return getOrCreate(stack1).uuid == getOrCreate(stack2).uuid;
    }

    public static LightsaberTag getOrCreate(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ID)) return create(stack);
        return new LightsaberTag(stack.getOrCreateTag().getCompound(ID));
    }

    public static ItemStack update(ItemStack stack, Consumer<LightsaberTag> consumer) {
        var lT = getOrCreate(stack);
        consumer.accept(lT);
        return lT.change(stack);
    }

    private static LightsaberTag create(ItemStack stack) {
        var lT = LightsaberContent.LIGHTSABER_DATA.get(Constants.withId("yoda")).toTag();
        lT.change(stack);
        return lT;
    }
}
