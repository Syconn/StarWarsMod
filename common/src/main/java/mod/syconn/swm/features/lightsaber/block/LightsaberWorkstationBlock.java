package mod.syconn.swm.features.lightsaber.block;

import mod.syconn.swm.block.TwoPartBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class LightsaberWorkstationBlock extends TwoPartBlock {

    public LightsaberWorkstationBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
