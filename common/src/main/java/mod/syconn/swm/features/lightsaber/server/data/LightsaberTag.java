package mod.syconn.swm.features.lightsaber.server.data;

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

public class LightsaberTag {

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
        this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("lightsaber/yoda");
        this.blades = NBTUtil.getList(tag.getCompound("blades"), BladeData::new);
        updateData(tag.getInt("version"));
    }

    private void updateData(int value) {
        this.version = value;
        var saved = LightsaberContent.LIGHTSABER_DATA.get(this.model);
        if (saved == null) Constants.LOG.warn("Invalid Lightsaber Tag for {}", this.model);
        else if (this.version != saved.version()) {
            var active = this.isActive();
            var tag = saved.toTag().save();

            this.uuid = tag.contains("uuid") ? tag.getUUID("uuid") : UUID.randomUUID();
            this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("yoda");
            this.blades = NBTUtil.getList(tag.getCompound("blades"), BladeData::new);
            this.blades.forEach(b -> b.active = active);
            this.version = saved.version();
        }
    }

    public double hiltLength() {
        if (this.getPrimaryBlade() == null) return 0f;
        return ((this.getPrimaryBlade().emitterPos.y() - 0.1) * 16 + 9.6) / 16;
    }

    public ItemStack getTemporary(boolean active, boolean singleBlade) {
        if (singleBlade && this.getPrimaryBlade() != null) this.getPrimaryBlade().active = active;
        this.getSecondaryBlades().forEach(b -> b.active = active && !singleBlade);
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return change(stack);
    }

    public ItemStack getTemporary(int blade, float lengthScale) {
        this.blades.forEach(b -> {
            b.active = false;
            b.transition = 0;
        });
        if (this.blades.size() >= blade) {
            final var bladeData = this.blades.get(blade);
            bladeData.active = true;
            bladeData.transition = 0;
            bladeData.bladeLengthScalar = Math.min(lengthScale, bladeData.bladeLengthScalar);
        }
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return change(stack);
    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public void togglePrimary() {
        if (this.getPrimaryBlade() != null) {
            var active = this.isActive();
            this.getPrimaryBlade().toggle();
            if (active) for (var blade : getSecondaryBlades()) blade.toggle(false);
        }
    }

    public void toggleAll() {
        if (this.getPrimaryBlade() != null) {
            if (this.isActive() && this.hasUnactive()) blades.forEach(b -> b.toggle(true));
            else {
                var active = !this.isActive();
                this.getPrimaryBlade().toggle();
                for (var blade : getSecondaryBlades()) blade.toggle(active);
            }
        }
    }

    public void toggleTo(boolean active) {
        if (this.getPrimaryBlade() != null) blades.forEach(b -> b.toggle(active));
    }

    public float getSize() {
        if (this.getPrimaryBlade().active) return this.getPrimaryBlade().getSize();
        else {
            for (var blade : this.getSecondaryBlades()) {
                if (blade.active) return blade.getSize();
            }
        }
        return 0;
    }

    public BladeData getPrimaryBlade() {
        return this.blades.isEmpty() ? null : this.blades.get(0);
    }

    public List<BladeData> getSecondaryBlades() {
        return this.blades.size() > 1 ? this.blades.subList(1, this.blades.size()) : List.of();
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

    public boolean isActive() {
        return this.blades.stream().anyMatch(b -> b.active);
    }

    public boolean hasUnactive() {
        return this.blades.stream().anyMatch(b -> !b.active);
    }

    public int getColor(int blade) {
        return this.blades.get(blade) != null ? this.blades.get(blade).color : -1;
    }

    public void setColor(int color) {
        this.blades.forEach(bladeData -> bladeData.color = color);
    }

    public void setColor(int blade, int color) {
        if (this.blades.get(blade) != null) this.blades.get(blade).color = color;
    }

    public void quickTurnoff() {
        this.blades.forEach(b -> {
            b.active = false;
            b.transition = 0;
        });
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
        var lT = LightsaberContent.LIGHTSABER_DATA.get(Constants.withId("lightsaber/yoda")).toTag();
        lT.change(stack);
        return lT;
    }
}
