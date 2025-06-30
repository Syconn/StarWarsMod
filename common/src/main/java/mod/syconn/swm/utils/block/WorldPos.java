package mod.syconn.swm.utils.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record WorldPos(ResourceKey<Level> level, BlockPos pos) {


}
