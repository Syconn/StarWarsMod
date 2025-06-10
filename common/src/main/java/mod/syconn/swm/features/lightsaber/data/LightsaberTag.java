package mod.syconn.swm.features.lightsaber.data;

import mod.syconn.swm.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.math.Ease;
import mod.syconn.swm.util.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class LightsaberTag {

    private static final String ID = "lightsaberData";
    private static final byte TRANSITION_TICKS = 8;

    private final String UUID = "uuid";
    private final String MODEL = "model";
    private final String STABLE = "stable";
    private final String ACTIVE = "active";
    private final String TRANSITION = "transition";
    private final String LENGTH = "length";
    private final String LENGTH_SCALAR = "length_scalar";
    private final String RADIUS = "radius";
    private final String COLOR = "color";
    private final String EMITTER_POSITIONS = "emitter_pos";

    public UUID uuid;
    public int model;
    public boolean stable;
    public boolean active;
    public byte transition;
    public double length;
    public double lengthScalar;
    public double radius;
    public int color;
    public List<Vec3> emitterPositions;

    public LightsaberTag(CompoundTag tag) {
        this.uuid = tag.hasUUID(UUID) ? tag.getUUID(UUID) : java.util.UUID.randomUUID();
        this.model = tag.getInt(MODEL);
        this.stable = tag.getBoolean(STABLE);
        this.active = tag.getBoolean(ACTIVE);
        this.transition = tag.getByte(TRANSITION);
        this.length = tag.getDouble(LENGTH);
        this.lengthScalar = tag.getDouble(LENGTH_SCALAR);
        this.radius = tag.getDouble(RADIUS);
        this.color = tag.getInt(COLOR);
        this.emitterPositions = NbtTools.getArray(tag.getCompound(EMITTER_POSITIONS), NbtTools::getVec3);
    }

    public LightsaberTag(UUID uuid, int model, boolean stable, boolean active, byte transition, double length, double lengthScalar, double radius,
                         int color, List<Vec3> emitterPositions) {
        this.uuid = uuid;
        this.model = model;
        this.stable = stable;
        this.active = active;
        this.transition = transition;
        this.length = length;
        this.lengthScalar = lengthScalar;
        this.radius = radius;
        this.color = color;
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
        var lT = LightsaberContent.LIGHTSABER_DATA.get(Constants.withId("mace")).toTag(2);
        lT.change(stack);
        return lT;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putUUID(UUID, uuid);
        tag.putInt(MODEL, model);
        tag.putBoolean(STABLE, stable);
        tag.putBoolean(ACTIVE, active);
        tag.putByte(TRANSITION, transition);
        tag.putDouble(LENGTH, length);
        tag.putDouble(LENGTH_SCALAR, lengthScalar);
        tag.putDouble(RADIUS, radius);
        tag.putInt(COLOR, color);
        tag.put(EMITTER_POSITIONS, NbtTools.putArray(emitterPositions, NbtTools::putVec3));
        return tag;
    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public void toggle() {
        if (transition != 0) return;
        transition = active ? -TRANSITION_TICKS : TRANSITION_TICKS;
        active = !active;
    }

    public void tick() {
        if (transition > 0) transition--;
        if (transition < 0) transition++;
    }

    public float getSize(float partialTicks) {
        if (transition == 0) return active ? 1 : 0;
        if (transition > 0) return Ease.outCubic(1 - (transition - partialTicks) / TRANSITION_TICKS);
        return Ease.inCubic(-(transition + partialTicks) / TRANSITION_TICKS);
    }
}
