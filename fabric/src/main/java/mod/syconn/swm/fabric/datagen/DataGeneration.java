package mod.syconn.swm.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        pack.addProvider(BlockLootTables::new);
        pack.addProvider(ModelProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(LangProvider::new);
        pack.addProvider(DatapackProvider::new);
        pack.addProvider(LightsaberDataProvider::new);
        pack.addProvider(RecipeProvider::new);
    }
}
