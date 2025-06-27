package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.fabric.client.data.LightsaberDefaults;
import mod.syconn.swm.server.recipes.LightsaberRecipe;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.server.Material;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.LIGHTSABER_WORKBENCH.get())
                .pattern("mdr")
                .pattern("nnn")
                .pattern("n n")
                .define('m', ModItems.MONITOR.get())
                .define('d', ModItems.DRILL.get())
                .define('r', ModItems.DRIVER.get())
                .define('n', Items.NETHERITE_INGOT)
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.NETHERITE_INGOT).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DRIVER.get())
                .pattern(" i ")
                .pattern(" i ")
                .pattern(" c ")
                .define('i', Items.IRON_INGOT)
                .define('c', Items.BLACK_CONCRETE)
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BLACK_CONCRETE).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DRILL.get())
                .pattern(" bd")
                .pattern("ri ")
                .pattern(" b ")
                .define('i', Items.IRON_BARS)
                .define('b', Items.BLACK_CONCRETE)
                .define('d', Items.RED_DYE)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BLACK_CONCRETE).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SCREEN.get())
                .pattern("iri")
                .pattern("rlr")
                .pattern("iri")
                .define('i', Items.IRON_BARS)
                .define('r', Items.REDSTONE)
                .define('l', Items.REDSTONE_LAMP)
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MONITOR.get())
                .pattern("idi")
                .pattern("isi")
                .pattern("iii")
                .define('i', Items.IRON_INGOT)
                .define('s', ModItems.SCREEN.get())
                .define('d', ModItems.DRIVER.get())
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
                .save(writer);

        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.ANAKIN, Material.of(Items.IRON_INGOT, 24), Material.of(Items.COAL, 22), Material.of(Items.REDSTONE, 12));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.AHSOKA, Material.of(Items.IRON_INGOT, 30), Material.of(Items.COAL, 26), Material.of(Items.REDSTONE, 12));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.DARK_SABER, Material.of(Items.IRON_INGOT, 14), Material.of(Items.COAL, 42), Material.of(Items.REDSTONE, 18),
                Material.of(Items.NETHERITE_INGOT, 2));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.MACE, Material.of(Items.IRON_INGOT, 14), Material.of(Items.COAL, 36), Material.of(Items.REDSTONE, 23),
                Material.of(Items.GOLD_INGOT, 26));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.KYLO, Material.of(Items.IRON_INGOT, 28), Material.of(Items.REDSTONE, 18), Material.of(Items.COAL, 42));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.TEMPLE_GUARD, Material.of(Items.QUARTZ, 42), Material.of(Items.REDSTONE, 18));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.KAL, Material.of(Items.COPPER_INGOT, 28), Material.of(Items.REDSTONE, 18), Material.of(Items.COAL, 42));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.LUKE, Material.of(Items.IRON_INGOT, 37), Material.of(Items.COPPER_INGOT, 16), Material.of(Items.REDSTONE_BLOCK, 5));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.MAUL, Material.of(Items.IRON_INGOT, 52), Material.of(Items.COAL, 38), Material.of(Items.REDSTONE_BLOCK, 12));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.OBI_WAN, Material.of(Items.IRON_INGOT, 12), Material.of(Items.COAL, 12), Material.of(Items.IRON_NUGGET, 24),
                Material.of(Items.NETHERITE_INGOT, 1), Material.of(Items.REDSTONE_LAMP, 1));
        this.lightsaber(writer, LightsaberDefaults.LightsaberTypes.YODA, Material.of(Items.IRON_INGOT, 16), Material.of(Items.COAL, 12), Material.of(Items.REDSTONE_TORCH, 4));
    }

    private void lightsaber(RecipeOutput output, LightsaberDefaults.LightsaberTypes type, Material<?>... materials) {
        LightsaberRecipe.Builder builder = LightsaberRecipe.builder(type.getData().toItem(), RecipeProvider::has, RecipeProvider::has);
        for(Material<?> material : materials) builder.requiresMaterial(material);
        builder.save(output, Constants.withId("lightsabers/" + type.getId()));
    }
}
