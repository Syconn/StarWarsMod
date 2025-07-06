package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.utils.client.HologramData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class HoloProjectorBlockEntity extends SyncedBlockEntity {

    private UUID playerRender = null;
    private HologramData hologramData = null;

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public void setHologramData(UUID player) {
        this.playerRender = player;
        markDirty();
    }

    public HologramData getHologramData() {
        return hologramData;
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("uuid")) this.playerRender = tag.getUUID("uuid");

        if (this.level != null && this.level.isClientSide) this.hologramData = new HologramData(this.playerRender);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (this.playerRender != null) tag.putUUID("uuid", this.playerRender);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoloProjectorBlockEntity blockEntity) {
        if (blockEntity.hologramData != null) {
            blockEntity.hologramData.tick();
//            blockEntity.markDirty();
        }
    }
}
