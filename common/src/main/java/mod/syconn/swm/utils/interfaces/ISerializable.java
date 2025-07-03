package mod.syconn.swm.utils.interfaces;

import net.minecraft.nbt.Tag;

public interface ISerializable<T extends Tag> {

    T writeTag();

}
