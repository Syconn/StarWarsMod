package mod.syconn.swm.forge.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.block.ModBlockStateProperties;
import mod.syconn.swm.util.block.TwoPart;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

public class BlockLootTables extends BlockLootSubProvider {

    public BlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    protected void generate() {
        this.add(ModBlocks.LIGHTSABER_WORKBENCH.get(), arg -> this.createSinglePropConditionTable(arg, ModBlockStateProperties.TWO_PART, TwoPart.RIGHT));
    }

    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> Constants.MOD.equals(ForgeRegistries.BLOCKS.getKey(block).getNamespace())).collect(Collectors.toSet());
    }
}
