package mod.syconn.swm.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(BlockLootTables::new);
        generator.addProvider(ModelProvider::new);
        generator.addProvider(BlockTagProvider::new);
        generator.addProvider(LangProvider::new);
        generator.addProvider(LightsaberDataProvider::new);
        generator.addProvider(RecipeProvider::new);
    }
}
