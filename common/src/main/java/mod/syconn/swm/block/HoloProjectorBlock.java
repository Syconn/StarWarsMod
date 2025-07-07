package mod.syconn.swm.block;

import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import mod.syconn.swm.utils.interfaces.IEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HoloProjectorBlock extends FaceAttachedHorizontalDirectionalBlock implements IEntityBlock {

    public HoloProjectorBlock() {
        super(BlockBehaviour.Properties.of().noCollission().strength(0.5F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(FACE) == AttachFace.CEILING)
            return Block.box(3, 15, 3, 13, 16, 13);
        else if (pState.getValue(FACE) == AttachFace.FLOOR)
            return Block.box(3, 0, 3, 13, 1, 13);
        else {
            if (pState.getValue(FACING) == Direction.NORTH)
                return Block.box(3, 3, 15, 13, 13, 16);
            if (pState.getValue(FACING) == Direction.SOUTH)
                return Block.box(3, 3, 0, 13, 13, 1);
            if (pState.getValue(FACING) == Direction.WEST)
                return Block.box(15, 3, 3, 16, 13, 13);
            return Block.box(0, 3, 3, 1, 13, 13);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HoloProjectorBlockEntity(pPos, pState);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
//        if (pLevel.isClientSide) { TODO UNCOMMENT LATER
//            EnvExecutor.runInEnv(Env.CLIENT, () -> () -> GameInstance.getClient().setScreen(ClientHooks.createHologramScreen(new WorldPos(pLevel.dimension(), pPos), false)));
//            return InteractionResult.SUCCESS;
//        }

        if (!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND && pLevel.getBlockEntity(pPos) instanceof HoloProjectorBlockEntity blockEntity) {
            blockEntity.setHologramData(pPlayer.getUUID());
            return InteractionResult.SUCCESS;
        }

        // TODO TESTING CODE
//        if (pLevel instanceof ServerLevel serverLevel) {
//            var caller = new HologramNetwork.Caller(pPlayer.getUUID(), new WorldPos(serverLevel.dimension(), pPos), false);
//            var network = HologramNetwork.get(serverLevel);
//            network.createCall(caller, List.of());
//            return InteractionResult.SUCCESS;
//        }

        return InteractionResult.PASS;
    }
}
