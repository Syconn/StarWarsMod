package mod.syconn.swm.features.lightsaber.blockentity;

import mod.syconn.swm.blockentity.SyncedBlockEntity;
import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LightsaberWorkbenchBlockEntity extends SyncedBlockEntity {

    private final SimpleContainer container = new SimpleContainer(1);

    public LightsaberWorkbenchBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), pWorldPosition, pBlockState);
        this.container.addListener(listener -> markDirty());
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

    public boolean hasItem() {
        return !container.getItem(0).isEmpty();
    }

    public ItemStack removeItem() {
        return LightsaberTag.getTemporary(container.removeItem(0, 1), true);
    }

    public void addItem(Player player, InteractionHand hand) {
        container.addItem(player.getItemInHand(hand).copyWithCount(1));
        player.getItemInHand(hand).shrink(1);
        LightsaberTag.update(container.getItem(0), LightsaberTag::toggle);
    }

    public SimpleContainer getContainer() {
        return container;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LightsaberWorkbenchBlockEntity blockEntity) {
        if (blockEntity.getContainer().getItem(0).getItem() instanceof LightsaberItem) LightsaberTag.update(blockEntity.getContainer().getItem(0), LightsaberTag::tick);
    }
}
