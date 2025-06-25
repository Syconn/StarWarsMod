package mod.syconn.swm.forge.datagen;

import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.forge.client.data.LightsaberDefaults;
import mod.syconn.swm.util.Constants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    public ItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Constants.MOD, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SCREEN.get());

        var builder = getBuilder(getId(ModItems.LIGHTSABER.get()).toString()).parent(generated("lightsaber/yoda"));
        for (var lightsaber : LightsaberDefaults.LightsaberTypes.values())
            builder.override().predicate(Constants.withId("model"), lightsaber.getData().model() * 0.1f).model(generated("lightsaber/" + lightsaber.getId())).end();
    }

    private ModelFile generated(String loc) {
        return new ModelFile.UncheckedModelFile(modLoc("item/" + loc));
    }

    private ResourceLocation getId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
