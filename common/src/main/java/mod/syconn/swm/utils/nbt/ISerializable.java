package mod.syconn.swm.utils.nbt;

import net.minecraft.nbt.Tag;

public interface ISerializable<T extends Tag> {

    T writeTag();
}
