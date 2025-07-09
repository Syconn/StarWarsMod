package mod.syconn.swm.features.lightsaber.data;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.NodeVec3;
import mod.syconn.swm.utils.generic.AnimationUtil;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class LightsaberTag {

    private static final String ID = "lightsaberData";
    private static final byte IGNITION_TICKS = 6;
    private static final byte RETRACTION_TICKS = 12;

    private final String UUID = "uuid";
    private final String MODEL = "model";
    private final String STABLE = "stable";
    private final String ACTIVE = "active";
    private final String TRANSITION = "transition";
    private final String RADIUS = "radius";
    private final String LENGTH_SCALAR = "lengthScalar";
    private final String COLOR = "color";
    private final String BLADE_TYPE = "bladeType";
    private final String EMITTER_POSITIONS = "vectors";

    public UUID uuid;
    public int model;
    public boolean stable;
    public boolean active;
    public float lengthScalar;
    public byte transition;
    public double radius;
    public int color;
    public String bladeType;
    public List<NodeVec3> emitterPositions;

    public LightsaberTag(CompoundTag tag) {
        this.uuid = tag.hasUUID(UUID) ? tag.getUUID(UUID) : java.util.UUID.randomUUID();
        this.model = tag.getInt(MODEL);
        this.stable = tag.getBoolean(STABLE);
        this.active = tag.getBoolean(ACTIVE);
        this.transition = tag.getByte(TRANSITION);
        this.lengthScalar = tag.getFloat(LENGTH_SCALAR);
        this.radius = tag.getDouble(RADIUS);
        this.color = tag.getInt(COLOR);
        this.bladeType = tag.contains(BLADE_TYPE) ? tag.getString(BLADE_TYPE) : "plasma";
        this.emitterPositions = NBTUtil.getList(tag.getCompound(EMITTER_POSITIONS), NodeVec3::getNode);
    }

    public LightsaberTag(UUID uuid, int model, boolean stable, float lengthScalar, boolean active, byte transition, double radius, int color, String bladeType, List<NodeVec3> emitterPositions) {
        this.uuid = uuid;
        this.model = model;
        this.stable = stable;
        this.active = active;
        this.transition = transition;
        this.lengthScalar = lengthScalar;
        this.radius = radius;
        this.color = color;
        this.bladeType = bladeType;
        this.emitterPositions = emitterPositions;
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

    public ItemStack getTemporary(boolean active, boolean singleBlade) {
        if (singleBlade) this.emitterPositions = List.of(this.emitterPositions.get(0));
        return getTemporary(this, active);
    }

    public static ItemStack getTemporary(ItemStack original, boolean active) {
        return getTemporary(LightsaberTag.getOrCreate(original), active);
    }

    public static ItemStack getTemporary(LightsaberTag original, boolean active) {
        original.active = active;
        var stack = new ItemStack(ModItems.LIGHTSABER.get());
        return original.change(stack);
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putUUID(UUID, this.uuid);
        tag.putInt(MODEL, this.model);
        tag.putBoolean(STABLE, this.stable);
        tag.putBoolean(ACTIVE, this.active);
        tag.putFloat(LENGTH_SCALAR, this.lengthScalar);
        tag.putByte(TRANSITION, this.transition);
        tag.putDouble(RADIUS, this.radius);
        tag.putInt(COLOR, this.color);
        tag.putString(BLADE_TYPE, this.bladeType);
        tag.put(EMITTER_POSITIONS, NBTUtil.putList(this.emitterPositions, NodeVec3::putNode));
        return tag;
    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public void toggle() {
        if (this.transition != 0) return;
        this.transition = this.active ? -RETRACTION_TICKS : IGNITION_TICKS;
        this.active = !this.active;
    }

    public void tick() {
        if (this.transition > 0) this.transition--;
        if (this.transition < 0) this.transition++;
    }

    public float getSize() {
        var partialTicks = StarWarsClient.getTickDelta();
        if (this.transition == 0) return this.active ? 1 : 0;
        if (this.transition > 0) return AnimationUtil.outCubic(1 - (this.transition - partialTicks) / IGNITION_TICKS);
        return AnimationUtil.inCubic(-(this.transition + partialTicks) / RETRACTION_TICKS);
    }
}
