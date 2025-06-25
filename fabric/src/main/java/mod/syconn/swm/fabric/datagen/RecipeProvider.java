package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.fabric.client.data.LightsaberDefaults;
import mod.syconn.swm.fabric.client.data.recipes.LightsaberRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
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

        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.ANAKIN)
                .addIngredient(ConventionalItemTags.IRON_INGOTS, 24)
                .addIngredient(Items.COAL, 22)
                .addIngredient(Items.REDSTONE, 12)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.AHSOKA)
                .addIngredient(ConventionalItemTags.IRON_INGOTS, 30)
                .addIngredient(Items.COAL, 26)
                .addIngredient(Items.REDSTONE, 12)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.DARK_SABER)
                .addIngredient(ConventionalItemTags.IRON_INGOTS, 14)
                .addIngredient(Items.COAL, 42)
                .addIngredient(Items.REDSTONE, 18)
                .addIngredient(Items.NETHERITE_INGOT, 2)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.MACE)
                .addIngredient(ConventionalItemTags.IRON_INGOTS, 14)
                .addIngredient(Items.COAL, 36)
                .addIngredient(Items.REDSTONE, 23)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.KYLO)
                .addIngredient(ConventionalItemTags.IRON_INGOTS, 28)
                .addIngredient(Items.REDSTONE, 18)
                .addIngredient(Items.COAL, 42)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.TEMPLE_GUARD)
                .addIngredient(Items.QUARTZ, 42)
                .addIngredient(Items.REDSTONE, 18)
                .addCriterion("has_quartz_ingot", has(Items.QUARTZ))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.KAL)
                .addIngredient(ConventionalItemTags.COPPER_INGOTS, 28)
                .addIngredient(Items.REDSTONE, 18)
                .addIngredient(Items.COAL, 42)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.COPPER_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.LUKE)
                .addIngredient(ConventionalItemTags.IRON_INGOTS, 37)
                .addIngredient(ConventionalItemTags.COPPER_INGOTS, 16)
                .addIngredient(Items.REDSTONE_BLOCK, 5)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_redstone_block", has(Items.REDSTONE_BLOCK))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.MAUL)
                .addIngredient(Items.IRON_INGOT, 52)
                .addIngredient(Items.COAL, 38)
                .addIngredient(Items.REDSTONE_BLOCK, 12)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .addCriterion("has_redstone_block", has(Items.REDSTONE_BLOCK))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.OBI_WAN)
                .addIngredient(Items.IRON_INGOT, 12)
                .addIngredient(Items.COAL, 12)
                .addIngredient(Items.IRON_NUGGET, 24)
                .addIngredient(ConventionalItemTags.NETHERITE_INGOTS, 1)
                .addIngredient(Items.REDSTONE_LAMP, 1)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .addCriterion("has_redstone_block", has(Items.REDSTONE_BLOCK))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.YODA)
                .addIngredient(Items.IRON_INGOT, 16)
                .addIngredient(Items.COAL, 12)
                .addIngredient(Items.REDSTONE_TORCH, 4)
                .addCriterion("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
    }
}
