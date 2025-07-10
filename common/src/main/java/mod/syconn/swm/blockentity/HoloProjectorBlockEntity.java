package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.client.HologramData;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoloProjectorBlockEntity extends SyncedBlockEntity { // TODO TO ITEM CONTROL

    private final Map<UUID, Vec3> renderables = new HashMap<>();
    private final Map<UUID, HologramData> renderers = new HashMap<>();
    private UUID callId = null;

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public void addEntity(UUID uuid, Vec3 vec) {
        var renderer = this.renderables.get(uuid);
        if (renderer == null || !renderer.equals(vec)) {
            renderables.put(uuid, vec);
            this.markDirty();
        }
    }

    public UUID getCallId() {
        return callId;
    }

    public void addCall(UUID callId) {
        if (this.callId != null && callId != null && this.level instanceof ServerLevel serverLevel)
            HologramNetwork.get(serverLevel).blockRemoved(this.callId, new WorldPos(serverLevel.dimension(), this.worldPosition));
        this.callId = callId;
        this.markDirty();
    }

    public Collection<HologramData> getHolograms() {
        return renderers.values();
    }

    @Override
    public void load(CompoundTag tag) {
        this.renderables.clear();
        this.renderables.putAll(NBTUtil.getMap(tag.getCompound("renderables"), NBTUtil::getUUID, NBTUtil::getVec3));
        this.callId = NBTUtil.getNullable(tag.getCompound("call"), NBTUtil::getUUID);

        if (this.level != null && this.level.isClientSide) {
            renderers.keySet().stream().filter(u -> !renderables.containsKey(u)).forEach(u -> this.renderers.get(u).endCall(() -> this.renderers.remove(u)));
            renderables.forEach((uuid, pos) -> {
                if (!renderers.containsKey(uuid)) this.renderers.put(uuid, new HologramData(uuid, pos, false));
                if (!renderers.get(uuid).getPosition().equals(pos)) this.renderers.put(uuid, this.renderers.get(uuid).setPosition(pos));
            });
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("renderables", NBTUtil.putMap(this.renderables, NBTUtil::putUUID, NBTUtil::putVec3));
        tag.put("call", NBTUtil.putNullable(this.callId, NBTUtil::putUUID));
    }
}
