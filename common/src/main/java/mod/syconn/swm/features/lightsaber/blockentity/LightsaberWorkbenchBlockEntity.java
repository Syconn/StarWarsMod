package mod.syconn.swm.features.lightsaber.blockentity;

import dev.architectury.hooks.item.ItemStackHooks;
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
import net.minecraft.world.level.block.state.BlockState;

public class LightsaberWorkbenchBlockEntity extends SyncedBlockEntity {

    private final SimpleContainer container = new SimpleContainer(1);
    private int ticks = 1;

    public LightsaberWorkbenchBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), pWorldPosition, pBlockState);
        this.container.addListener(listener -> {
            var stack = container.getItem(0);
            if (stack.getItem() instanceof LightsaberItem && LightsaberTag.getOrCreate(stack).active) LightsaberTag.update(stack, LightsaberTag::toggle);
            markDirty();
        });
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
        var stack = container.removeItemNoUpdate(0);
        LightsaberTag.update(stack, tag -> {
            if (!tag.active) tag.toggle();
        });
        return stack;
    }

    public void addItem(Player player, InteractionHand hand) {
        container.addItem(ItemStackHooks.copyWithCount(player.getItemInHand(hand), 1));
        player.getItemInHand(hand).shrink(1);
    }

    public SimpleContainer getContainer() {
        return container;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LightsaberWorkbenchBlockEntity blockEntity) {
        var stack = blockEntity.getContainer().getItem(0);
        if (stack.getItem() instanceof LightsaberItem && LightsaberTag.getOrCreate(stack).getSize() > 0) {
            if (blockEntity.ticks <= 0) {
                LightsaberTag.update(stack, LightsaberTag::tick);
                blockEntity.ticks = 1;
                blockEntity.markDirty();
            }
            blockEntity.ticks--;
        }
    }
}
