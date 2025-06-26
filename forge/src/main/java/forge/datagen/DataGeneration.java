package forge.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mod.syconn.swm.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new RecipeProvider(generator));
        generator.addProvider(event.includeClient(), new LangProvider(generator, "en_us"));
        generator.addProvider(event.includeClient(), new BlockTagProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModelProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockModelProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new LightsaberDataProvider(generator));
        generator.addProvider(event.includeClient(), new LootTableProvider(generator) {
            @Override
            protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
                return ImmutableList.of(Pair.of(BlockLootTables::new, LootContextParamSets.BLOCK));
            }
        });
    }
}
