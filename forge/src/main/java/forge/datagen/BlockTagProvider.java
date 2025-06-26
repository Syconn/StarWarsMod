package forge.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Constants.MOD, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.LIGHTSABER_WORKBENCH.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.LIGHTSABER_WORKBENCH.get());
    }
}
