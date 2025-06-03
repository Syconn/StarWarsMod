package mod.syconn.swm.forge.client.datagen;

import mod.syconn.swm.forge.client.data.LightsaberDefaults;
import mod.syconn.swm.registry.ModItems;
import mod.syconn.swm.util.Constants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModels extends ItemModelProvider {

    public ItemModels(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Constants.MOD, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ItemModelBuilder builder = getBuilder(getItemId(ModItems.LIGHTSABER.get()).toString()).parent(generated("lightsaber/yoda"));

        for (var lightsaber : LightsaberDefaults.LightsaberTypes.values())
            builder.override().predicate(Constants.withId("model"), lightsaber.getData().model() * 0.1f).model(generated("lightsaber/" + lightsaber.getId())).end();
    }

    private ModelFile generated(String loc) {
        return new ModelFile.UncheckedModelFile(modLoc("item/" + loc));
    }

    private ResourceLocation getItemId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
