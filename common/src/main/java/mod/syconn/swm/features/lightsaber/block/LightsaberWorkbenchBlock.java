package mod.syconn.swm.features.lightsaber.block;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import mod.syconn.swm.block.TwoPartBlock;
import mod.syconn.swm.core.ModMenus;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
        if (player instanceof ServerPlayer serverPlayer) {
            var be = level.getBlockEntity(pos) instanceof LightsaberWorkbenchBlockEntity ? level.getBlockEntity(pos) : level.getBlockEntity(pos.relative(getOtherPart(state)));

            if (be instanceof LightsaberWorkbenchBlockEntity) {
                MenuRegistry.openExtendedMenu(serverPlayer, new ExtendedMenuProvider() {
                    public void saveExtraData(FriendlyByteBuf buf) {
                        System.out.println("WRITING POSITION");
                        buf.writeBlockPos(pos);
                    }

                    public Component getDisplayName() {
                        return Component.literal("Lightsaber Workbench");
                    }

                    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                        return ModMenus.LIGHTSABER_WORKBENCH.get().create(i, inventory);
                    }
                });
            }
            return InteractionResult.SUCCESS;
        }
// player.awardStat(this.getOpenChestStat()); TODO MAYBE
        return InteractionResult.PASS;
    }

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
}
