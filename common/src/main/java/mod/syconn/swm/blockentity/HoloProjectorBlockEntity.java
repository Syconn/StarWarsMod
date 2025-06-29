package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HoloProjectorBlockEntity extends SyncedBlockEntity {

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoloProjectorBlockEntity holoProjectorBlockEntity) {

    }
}
