package forge.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import mod.syconn.swm.util.block.TwoPart;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class BlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        this.add(ModBlocks.LIGHTSABER_WORKBENCH.get(), arg -> createSinglePropConditionTable(arg, ModBlockStateProperties.TWO_PART, TwoPart.RIGHT));
    }

    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> Constants.MOD.equals(ForgeRegistries.BLOCKS.getKey(block).getNamespace())).collect(Collectors.toSet());
    }
}
