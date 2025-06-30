package mod.syconn.swm.utils.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NbtTools {

    public static <T> List<T> getArray(CompoundTag tag, Function<CompoundTag, T> function) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < tag.getInt("len"); i++) list.add(function.apply(tag.getCompound(String.valueOf(i))));
        return list;
    }

    public static <T> CompoundTag putArray(List<T> elements, Function<T, CompoundTag> function) {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < elements.size(); i++) tag.put(String.valueOf(i), function.apply(elements.get(i)));
        tag.putInt("len", elements.size());
        return tag;
    }

    public static <T extends Enum<T>> T getEnum(Class<T> enumClass, CompoundTag tag) {
        return enumClass.getEnumConstants()[tag.getInt("enumValue")];
    }

    public static CompoundTag writeEnum(Enum<?> value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("enumValue", value.ordinal());
        return tag;
    }

    public static Vec3 getVec3(CompoundTag tag) {
        return new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public static CompoundTag putVec3(Vec3 vec3) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }
}
