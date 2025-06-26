package forge.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.LIGHTSABER_WORKBENCH.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.LIGHTSABER_WORKBENCH.get());
    }
}
