package forge.client.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import forge.client.data.LightsaberDefaults;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.server.StackedIngredient;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LightsaberRecipeBuilder {

    private final LightsaberDefaults.LightsaberTypes type;
    private final List<StackedIngredient> ingredients = new ArrayList<>();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
    private final List<ICondition> conditions = new ArrayList<>();

    public LightsaberRecipeBuilder(LightsaberDefaults.LightsaberTypes type) {
        this.type = type;
    }

    public LightsaberRecipeBuilder addIngredient(ItemLike item, int count) {
        this.ingredients.add(StackedIngredient.of(item, count));
        return this;
    }

    public LightsaberRecipeBuilder addIngredient(TagKey<Item> item, int count) {
        this.ingredients.add(StackedIngredient.of(item, count));
        return this;
    }

    public LightsaberRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterionIn) {
        this.advancementBuilder.addCriterion(name, criterionIn);
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        ResourceLocation resourcelocation = Constants.withId(this.type.getId());
        this.build(consumer, resourcelocation);
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        this.advancementBuilder.parent(ResourceLocation.withDefaultNamespace("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(id, this.type, this.ingredients, this.conditions, this.advancementBuilder,
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/" + RecipeCategory.COMBAT.getFolderName() + "/" + id.getPath())));
    }

    private void validate(ResourceLocation id) {
        if(this.advancementBuilder.getCriteria().isEmpty())throw new IllegalStateException("No way of obtaining recipe " + id);
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final LightsaberDefaults.LightsaberTypes type;
        private final List<StackedIngredient> ingredients;
        private final List<ICondition> conditions;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, LightsaberDefaults.LightsaberTypes type, List<StackedIngredient> ingredients, List<ICondition> conditions, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.type = type;
            this.ingredients = ingredients;
            this.conditions = conditions;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            JsonArray conditions = new JsonArray();
            this.conditions.forEach(condition -> conditions.add(CraftingHelper.serialize(condition)));
            if(!conditions.isEmpty()) json.add("conditions", conditions);

            JsonArray materials = new JsonArray();
            this.ingredients.forEach(ingredient -> materials.add(ingredient.toJson()));
            json.add("materials", materials);

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.type.getData().toItem().getItem())).toString());
            resultObject.addProperty("nbt", Objects.requireNonNull(this.type.getData().toItem().getTag()).getAsString());
            json.add("result", resultObject);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ModRecipes.LIGHTSABER_SERIALIZER.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
