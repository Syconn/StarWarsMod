package mod.syconn.swm.features.lightsaber.blockentity;

import mod.syconn.swm.blockentity.SyncedBlockEntity;
import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightsaberWorkbenchBlockEntity extends SyncedBlockEntity {

    private final SimpleContainer container = new SimpleContainer(1);
    private int ticks = 1;

    public LightsaberWorkbenchBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), pWorldPosition, pBlockState);
        this.container.addListener(listener -> {
            var stack = container.getItem(0);
            if (stack.getItem() instanceof LightsaberItem && LightsaberComponent.getOrCreate(stack).active()) LightsaberComponent.update(stack, LightsaberComponent::toggle);
            markDirty();
        });
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", this.container.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory", 9)) this.container.fromTag(tag.getList("inventory", 10), registries);
    }

    public boolean hasItem() {
        return !container.getItem(0).isEmpty();
    }

    public ItemStack removeItem() {
        return LightsaberComponent.update(container.removeItem(0, 1), c -> !c.active() ? c.toggle() : c);
    }

    public void addItem(Player player, InteractionHand hand) {
        container.addItem(player.getItemInHand(hand).copyWithCount(1));
        player.getItemInHand(hand).shrink(1);
    }

    public SimpleContainer getContainer() {
        return container;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LightsaberWorkbenchBlockEntity blockEntity) {
        if (blockEntity.getContainer().getItem(0).getItem() instanceof LightsaberItem && blockEntity.ticks <= 0) {
            LightsaberComponent.update(blockEntity.getContainer().getItem(0), LightsaberComponent::tick);
            blockEntity.ticks = 1;
            blockEntity.markDirty();
        }
        blockEntity.ticks--;
    }
}
