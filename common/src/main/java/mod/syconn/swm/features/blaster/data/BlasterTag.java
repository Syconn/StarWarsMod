package mod.syconn.swm.features.blaster.data;

import net.minecraft.nbt.CompoundTag;

public class BlasterTag {

    private static final String ID = "blasterData";

    public BlasterTag(CompoundTag tag) {
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        return tag;
    }
}
