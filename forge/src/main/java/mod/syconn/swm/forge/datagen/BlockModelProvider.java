package mod.syconn.swm.forge.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockModelProvider extends BlockStateProvider {

    public BlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD, existingFileHelper);
    }

    protected void registerStatesAndModels() {
        getVariantBuilder(ModBlocks.LIGHTSABER_WORKBENCH.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(generated(getId(ModBlocks.LIGHTSABER_WORKBENCH.get()).getPath() + (state.getValue(ModBlockStateProperties.TWO_PART).right() ? "_right" : "_left")))
                .rotationY(state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH ? -90 : state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST ? 180
                        : state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST ? 0 : 90)
                .uvLock(false)
                .build());
    }

    private ModelFile generated(String loc) {
        return new ModelFile.UncheckedModelFile(modLoc("block/" + loc));
    }

    private ResourceLocation getId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
