package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.addAll(BlockTags.NEEDS_IRON_TOOL, ModBlocks.LIGHTSABER_WORKBENCH.get());
        this.addAll(BlockTags.MINEABLE_WITH_PICKAXE, ModBlocks.LIGHTSABER_WORKBENCH.get());
    }

    private void addAll(TagKey<Block> tagKey, Block... blocks) {
        var tag = this.tag(tagKey);
        for (Block block : blocks) tag.add(reverseLookup(block));
    }
}
