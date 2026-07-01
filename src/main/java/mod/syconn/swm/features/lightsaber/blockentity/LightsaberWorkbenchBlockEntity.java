package mod.syconn.swm.features.lightsaber.blockentity;

import mod.syconn.swm.api.blockentity.SyncedBlockEntity;
import mod.syconn.swm.registry.ModBlockEntities;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
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

    public LightsaberWorkbenchBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LIGHTSABER_WORKBENCH.get(), pWorldPosition, pBlockState);
        this.container.addListener(listener -> {
            if (listener.getItem(0).getItem() instanceof LightsaberItem) LightsaberTag.update(listener.getItem(0), lT -> lT.toggleTo(false));
            this.markDirty();
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
        return !this.container.getItem(0).isEmpty();
    }

    public ItemStack removeItem() {
        var stack = LightsaberTag.update(this.container.removeItem(0, this.container.getItem(0).getCount()), lT -> lT.toggleTo(true));
        this.markDirty();
        return stack;
    }

    public void addItem(Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand).copyWithCount(1);
        if (stack.getItem() instanceof LightsaberItem) {
            LightsaberTag.update(stack, lT -> lT.toggleTo(false));
            this.container.addItem(stack);
            player.getItemInHand(hand).shrink(1);
            this.markDirty();
        }
    }

    public SimpleContainer getContainer() {
        return this.container;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LightsaberWorkbenchBlockEntity blockEntity) {
        if (blockEntity.getContainer().getItem(0).getItem() instanceof LightsaberItem) {
            LightsaberTag.update(blockEntity.getContainer().getItem(0), LightsaberTag::tick);
            blockEntity.markDirty();
        }
    }
}
