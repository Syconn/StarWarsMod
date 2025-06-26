package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import mod.syconn.swm.util.block.TwoPart;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.data.loot.BlockLoot;

public class BlockLootTables extends FabricBlockLootTableProvider {

    public BlockLootTables(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateBlockLootTables() {
        this.add(ModBlocks.LIGHTSABER_WORKBENCH.get(), arg -> createSinglePropConditionTable(arg, ModBlockStateProperties.TWO_PART, TwoPart.RIGHT));
    }
}
