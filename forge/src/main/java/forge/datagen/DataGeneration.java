package forge.datagen;

import mod.syconn.swm.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new RecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new LangProvider(generator.getPackOutput(), "en_us"));
        generator.addProvider(event.includeClient(), new LootTableProvider(generator.getPackOutput(), Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeClient(), new BlockTagProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new DatapackBuiltinEntriesProvider(generator.getPackOutput(), event.getLookupProvider(), DatapackProvider.BUILDER, Set.of(Constants.MOD)));
        generator.addProvider(event.includeClient(), new LightsaberDataProvider(generator.getPackOutput()));
    }
}
