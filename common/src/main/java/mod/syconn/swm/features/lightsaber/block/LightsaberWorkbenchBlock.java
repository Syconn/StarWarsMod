package mod.syconn.swm.features.lightsaber.block;

import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.registry.menu.MenuRegistry;
import mod.syconn.swm.block.TwoPartBlock;
import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberAssemblerMenu;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberWorkbenchMenu;
import mod.syconn.swm.util.block.EntityBlockExtended;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import mod.syconn.swm.util.nbt.ItemStackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightsaberWorkbenchBlock extends TwoPartBlock implements EntityBlockExtended {

    public LightsaberWorkbenchBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var useHand = player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof LightsaberItem ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        var assembler = !(level.getBlockEntity(pos) instanceof LightsaberWorkbenchBlockEntity);
        if (level.getBlockEntity(getBlockEntityPos(level, pos, state)) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
            if (player.isCrouching() && blockEntity.hasItem()) {
                if (player.getItemInHand(useHand).isEmpty()) player.setItemSlot(useHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, blockEntity.removeItem());
                else ItemStackHelper.giveItem(player, blockEntity.removeItem());
                return InteractionResult.SUCCESS;
            } else if (player.getItemInHand(useHand).getItem() instanceof LightsaberItem && !blockEntity.hasItem()) {
                blockEntity.addItem(player, useHand);
                return InteractionResult.SUCCESS;
            } else if (assembler) {
                if (player.level.isClientSide) return InteractionResult.SUCCESS;
                MenuRegistry.openExtendedMenu((ServerPlayer) player, LightsaberAssemblerMenu.menu(getBlockEntityPos(level, pos, state)), buf -> buf.writeBlockPos(getBlockEntityPos(level, pos, state)));
                return InteractionResult.SUCCESS;
            } else if (!player.isCrouching()) {
                if (player.level.isClientSide) return InteractionResult.SUCCESS;
                MenuRegistry.openExtendedMenu((ServerPlayer) player, LightsaberWorkbenchMenu.menu(getBlockEntityPos(level, pos, state)), buf -> buf.writeBlockPos(getBlockEntityPos(level, pos, state)));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) { // TODO LIGHTSABER ASSEMBLER DROPS ITEM WHEN BROKE IN CREATIVE
        if (!state.is(newState.getBlock()) && level.getBlockEntity(getBlockEntityPos(level, pos, state)) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
            Containers.dropContents(level, pos, blockEntity.getContainer());
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
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

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchBlockEntity::tick);
    }

    private BlockPos getBlockEntityPos(@NotNull Level level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof LightsaberWorkbenchBlockEntity ? pos : pos.relative(getOtherPart(state));
    }
}
