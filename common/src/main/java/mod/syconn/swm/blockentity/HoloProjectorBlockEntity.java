package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.client.HologramData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoloProjectorBlockEntity extends SidedSyncedBlockEntity {

    private final List<WorldPos> connections = new ArrayList<>();
    private HologramData hologramData = null;

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public void joinCall(List<HologramNetwork.Caller> calls) {
        this.connections.clear();
        this.connections.addAll(calls.stream().map(HologramNetwork.Caller::location).filter(Objects::nonNull).toList());
        System.out.println(this.connections);
    }

    public void setHologramData(HologramData hologramData) {
        this.hologramData = hologramData;
    }

    public HologramData getHologramData() {
        return hologramData;
    }

    @Override
    CompoundTag saveSynced() {
        var tag = new CompoundTag();
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }
}
