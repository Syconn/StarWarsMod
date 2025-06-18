package mod.syconn.swm.features.lightsaber.blockentity;

import mod.syconn.swm.blockentity.SyncedBlockEntity;
import mod.syconn.swm.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.state.BlockState;

public class LightsaberWorkbenchBlockEntity extends SyncedBlockEntity {

    private final SimpleContainer container = new SimpleContainer(1);

    public LightsaberWorkbenchBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", this.container.createTag());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("inventory", 9)) this.container.fromTag(tag.getList("inventory", 10));
    }

    public SimpleContainer getContainer() {
        return container;
    }
}
