package mod.syconn.swm.forge.client.datagen;

import mod.syconn.swm.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new LangProvider(generator.getPackOutput(), "en_us"));
        generator.addProvider(event.includeClient(), new ItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new DatapackBuiltinEntriesProvider(generator.getPackOutput(), event.getLookupProvider(), DatapackProvider.BUILDER, Set.of(Constants.MOD)));
        generator.addProvider(event.includeClient(), new LightsaberDataProvider(generator.getPackOutput()));
    }
}
