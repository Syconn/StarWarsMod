package mod.syconn.swm.utils.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record WorldPos(ResourceKey<Level> level, BlockPos pos) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WorldPos wp) {
            return wp.level.equals(this.level) && wp.pos.equals(this.pos);
        }
        return false;
    }

    public static WorldPos from(CompoundTag tag) {
        return new WorldPos(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("level"))), NbtUtils.readBlockPos(tag.getCompound("pos")));
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putString("level", this.level.toString());
        tag.put("pos", NbtUtils.writeBlockPos(this.pos));
        return tag;
    }
}
