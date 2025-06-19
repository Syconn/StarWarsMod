package mod.syconn.swm.features.lightsaber.block;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import mod.syconn.swm.block.TwoPartBlock;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberWorkbenchMenu;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LightsaberWorkbenchBlock extends TwoPartBlock implements EntityBlock {

    public LightsaberWorkbenchBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player instanceof ServerPlayer sp && !player.isCrouching() && level.getBlockEntity(getBlockEntityPos(level, pos, state)) instanceof LightsaberWorkbenchBlockEntity) {
            MenuRegistry.openExtendedMenu(sp, new ExtendedMenuProvider() {
                public void saveExtraData(FriendlyByteBuf buf) {
                    buf.writeBlockPos(getBlockEntityPos(level, pos, state));
                }

                public Component getDisplayName() {
                    return Component.literal("Lightsaber Workbench");
                }

                public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return new LightsaberWorkbenchMenu(i, inventory, getBlockEntityPos(level, pos, state));
                }
            });
            // player.awardStat(this.getOpenChestStat()); TODO MAYBE
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(getBlockEntityPos(level, pos, state)) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
            Containers.dropContents(level, pos, blockEntity.getContainer());
            level.updateNeighbourForOutputSignal(pos, this);
        }
        else super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(ModBlockStateProperties.TWO_PART).right()) return new LightsaberWorkbenchBlockEntity(pos, state);
        return null;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, param);
    }

    private BlockPos getBlockEntityPos(Level level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof LightsaberWorkbenchBlockEntity ? pos : pos.relative(getOtherPart(state));
    }
}
