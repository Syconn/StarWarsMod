package mod.syconn.swm.forge.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.forge.client.data.LightsaberDefaults;
import mod.syconn.swm.forge.client.data.recipes.LightsaberRecipeBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.F_11D.get())
//                .pattern("d b")
//                .pattern("iii")
//                .pattern(" ri")
//                .define('d', Items.RED_DYE)
//                .define('b', Items.LIGHT_BLUE_DYE)
//                .define('i', Items.IRON_INGOT)
//                .define('r', Items.REDSTONE)
//                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.MAP, Items.PAPER).build()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.HOLO_PROJECTOR.get())
//                .pattern("rgr")
//                .pattern("rnr")
//                .pattern("rrr")
//                .define('r', Items.REDSTONE)
//                .define('n', Items.NETHERITE_INGOT)
//                .define('g', Items.GOLD_INGOT)
//                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
//                .save(writer);
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
                .addIngredient(Tags.Items.INGOTS_IRON, 24)
                .addIngredient(Items.COAL, 22)
                .addIngredient(Items.REDSTONE, 12)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.AHSOKA)
                .addIngredient(Tags.Items.INGOTS_IRON, 30)
                .addIngredient(Items.COAL, 26)
                .addIngredient(Items.REDSTONE, 12)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.DARK_SABER)
                .addIngredient(Tags.Items.INGOTS_IRON, 14)
                .addIngredient(Items.COAL, 42)
                .addIngredient(Items.REDSTONE, 18)
                .addIngredient(Items.NETHERITE_INGOT, 2)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.MACE)
                .addIngredient(Tags.Items.INGOTS_IRON, 14)
                .addIngredient(Items.COAL, 36)
                .addIngredient(Items.REDSTONE, 23)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.KYLO)
                .addIngredient(Tags.Items.INGOTS_IRON, 28)
                .addIngredient(Items.REDSTONE, 18)
                .addIngredient(Items.COAL, 42)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.TEMPLE_GUARD)
                .addIngredient(Items.QUARTZ, 42)
                .addIngredient(Items.REDSTONE, 18)
                .addCriterion("has_quartz_ingot", has(Items.QUARTZ))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.KAL)
                .addIngredient(Tags.Items.INGOTS_COPPER, 28)
                .addIngredient(Items.REDSTONE, 18)
                .addIngredient(Items.COAL, 42)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_COPPER))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.LUKE)
                .addIngredient(Tags.Items.INGOTS_IRON, 37)
                .addIngredient(Tags.Items.INGOTS_COPPER, 16)
                .addIngredient(Items.REDSTONE_BLOCK, 5)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_redstone_block", has(Items.REDSTONE_BLOCK))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.MAUL)
                .addIngredient(Items.IRON_INGOT, 52)
                .addIngredient(Items.COAL, 38)
                .addIngredient(Items.REDSTONE_BLOCK, 12)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .addCriterion("has_redstone_block", has(Items.REDSTONE_BLOCK))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.OBI_WAN)
                .addIngredient(Items.IRON_INGOT, 12)
                .addIngredient(Items.COAL, 12)
                .addIngredient(Items.IRON_NUGGET, 24)
                .addIngredient(Tags.Items.INGOTS_NETHERITE, 1)
                .addIngredient(Items.REDSTONE_LAMP, 1)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .addCriterion("has_redstone_block", has(Items.REDSTONE_BLOCK))
                .build(writer);
        new LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes.YODA)
                .addIngredient(Items.IRON_INGOT, 16)
                .addIngredient(Items.COAL, 12)
                .addIngredient(Items.REDSTONE_TORCH, 4)
                .addCriterion("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .addCriterion("has_coal_ingot", has(Items.COAL))
                .build(writer);
    }
}
