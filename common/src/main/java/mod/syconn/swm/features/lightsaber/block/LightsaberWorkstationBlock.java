package mod.syconn.swm.features.lightsaber.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class LightsaberWorkstationBlock extends Block {

    public LightsaberWorkstationBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
