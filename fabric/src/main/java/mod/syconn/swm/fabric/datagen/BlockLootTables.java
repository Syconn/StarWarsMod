package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import mod.syconn.swm.util.block.TwoPart;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class BlockLootTables extends FabricBlockLootTableProvider {

    public BlockLootTables(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.add(ModBlocks.LIGHTSABER_WORKBENCH.get(), arg -> this.createSinglePropConditionTable(arg, ModBlockStateProperties.TWO_PART, TwoPart.RIGHT));
    }
}
