package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.generic.MapUtil;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HoloProjectorBlockEntity extends SyncedBlockEntity {

    private final Map<UUID, Vec3> renderables = new HashMap<>();
    private UUID callId = null;

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoloProjectorBlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            var networkData = HologramNetwork.get(serverLevel).getBlockData(blockEntity.callId);
            if (networkData != null && !networkData.isEmpty()) {
                var update = false;
                var map = networkData.entrySet().stream().filter(e -> !e.getKey().equals(new WorldPos(level.dimension(), pos))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                var entities = map.values().stream().flatMap(Collection::stream).toList();
                var removals = Map.copyOf(blockEntity.renderables).keySet().stream().filter(u -> !entities.contains(u)).toList();
                if (!removals.isEmpty()) update = true;
                removals.forEach(blockEntity.renderables::remove); // TODO ANIMATION OUT
                for (var entry : map.entrySet()) {
                    for (var uuid : entry.getValue()) {
                        if (!blockEntity.renderables.containsKey(uuid) || !blockEntity.renderables.get(uuid).equals(entry.getKey())) {
                            var player = level.getServer().getLevel(entry.getKey().level()).getPlayerByUUID(uuid);
                            blockEntity.renderables.put(uuid, player == null ? new Vec3(0, 0, 0) : player.position().subtract(entry.getKey().toVector()));
                            update = true;
                        }
                    }
                }
                if (update) blockEntity.markDirty();
            } else if (!blockEntity.renderables.isEmpty()) {
                blockEntity.renderables.clear();
                blockEntity.callId = null;
                blockEntity.markDirty();
            }
        }
    }

    public UUID getCallId() {
        return callId;
    }

    public Map<UUID, Vec3> getRenderables() {
        return renderables;
    }

    public void addCall(UUID callId) {
        this.renderables.clear();
        if (this.callId != null && callId != null && this.level instanceof ServerLevel serverLevel)
            HologramNetwork.get(serverLevel).blockRemoved(this.callId, new WorldPos(serverLevel.dimension(), this.worldPosition));
        this.callId = callId;
        this.markDirty();
    }

    @Override
    public void load(CompoundTag tag) {
        this.renderables.clear();
        this.renderables.putAll(NBTUtil.getMap(tag.getCompound("renderables"), NBTUtil::getUUID, NBTUtil::getVec3));
        this.callId = NBTUtil.getNullable(tag.getCompound("call"), NBTUtil::getUUID);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("renderables", NBTUtil.putMap(this.renderables, NBTUtil::putUUID, NBTUtil::putVec3));
        tag.put("call", NBTUtil.putNullable(this.callId, NBTUtil::putUUID));
    }
}
