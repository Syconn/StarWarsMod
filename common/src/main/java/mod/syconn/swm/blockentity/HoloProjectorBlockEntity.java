package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.utils.client.HologramData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class HoloProjectorBlockEntity extends SyncedBlockEntity { // TODO TO ITEM CONTROL

    private UUID playerRender = null;
    private HologramData hologramData = null;

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public void setHologramData(UUID player) {
        this.playerRender = !player.equals(this.playerRender) ? player : null;
        markDirty();
    }

    public HologramData getHologramData() {
        return hologramData;
    }

    @Override
    public void load(CompoundTag tag) {
        this.playerRender = tag.contains("uuid") ? tag.getUUID("uuid") : null;

        if (this.level != null && this.level.isClientSide) this.hologramData = this.playerRender != null ? new HologramData(this.playerRender) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (this.playerRender != null) tag.putUUID("uuid", this.playerRender);
        tag.putInt("update", 1); // TODO DOESN'T LOAD IF NO TAG CHANGED - Fix
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoloProjectorBlockEntity blockEntity) {
        if (blockEntity.hologramData != null) {
            blockEntity.hologramData.tick();
//            blockEntity.markDirty();
        }
    }
}
