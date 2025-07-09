package mod.syconn.swm.blockentity;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.utils.client.HologramData;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class HoloProjectorBlockEntity extends SyncedBlockEntity { // TODO TO ITEM CONTROL

    private final List<UUID> renderables = new ArrayList<>();
    private final Map<UUID, HologramData> renderers = new HashMap<>();

    public HoloProjectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.HOLO_PROJECTOR.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HoloProjectorBlockEntity blockEntity) {
        var entities = level.getEntitiesOfClass(Player.class, new AABB(pos).move(0, 1, 0).inflate(3.0)).stream().map(Entity::getUUID).toList();
        var rem = blockEntity.renderables.stream().filter(u -> !entities.contains(u)).toList();
        var add = entities.stream().filter(u -> !blockEntity.renderables.contains(u)).toList();
        rem.forEach(blockEntity.renderables::remove);
        blockEntity.renderables.addAll(add);
        if (!rem.isEmpty() || !add.isEmpty()) blockEntity.markDirty();
    }

    public Collection<HologramData> getHolograms() {
        return renderers.values();
    }

    @Override
    public void load(CompoundTag tag) {
        this.renderables.clear();
        this.renderables.addAll(NBTUtil.getList(tag.getCompound("renderables"), NBTUtil::getUUID));

        if (this.level != null && this.level.isClientSide) {
//            renderables.stream().filter(u -> !renderers.containsKey(u)).forEach(u -> this.renderers.get(u).endCall(() -> this.renderers.remove(u)));
            renderables.forEach(uuid -> { if (!renderers.containsKey(uuid)) this.renderers.put(uuid, new HologramData(uuid, false)); });
        }

        System.out.println(this.renderers);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("renderables", NBTUtil.putList(this.renderables, NBTUtil::putUUID));
    }
}
