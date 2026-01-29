package mod.syconn.swm.utils.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface ThrowableProjectile {

    void writeSpawnData(CompoundTag tag);
    void readSpawnData(CompoundTag tag);
}
