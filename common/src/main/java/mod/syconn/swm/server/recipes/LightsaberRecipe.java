package mod.syconn.swm.server.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.util.server.Material;
import mod.syconn.swm.util.server.StackedIngredient;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

public record LightsaberRecipe(NonNullList<StackedIngredient> materials, ItemStack result) implements Recipe<Container> {

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.LIGHTSABER.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.LIGHTSABER_SERIALIZER.get();
    }

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(Container container, RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess access) {
        return this.result;
    }

    public static Builder builder(ItemStack result, Function<ItemLike, Criterion<?>> hasItem, Function<TagKey<Item>, Criterion<?>> hasTag) {
        return new Builder(result, hasItem, hasTag);
    }

    public static class Serializer implements RecipeSerializer<LightsaberRecipe> {
        public static final Codec<LightsaberRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(StackedIngredient.CODEC.listOf().fieldOf("materials").flatXmap(materials -> {
            var inputs = materials.stream().filter((ingredient) -> !ingredient.ingredient().isEmpty() || ingredient.count() <= 0).toArray(StackedIngredient[]::new);
            return DataResult.success(NonNullList.of(StackedIngredient.EMPTY, inputs));
        }, DataResult::success).forGetter(o -> o.materials), ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").
                forGetter(recipe -> recipe.result)).apply(builder, LightsaberRecipe::new));

        @Override
        public @NotNull Codec<LightsaberRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull LightsaberRecipe fromNetwork(FriendlyByteBuf buffer) {
            var materialCount = buffer.readInt();
            var materials = NonNullList.withSize(materialCount, StackedIngredient.EMPTY);
            IntStream.range(0, materialCount).forEach(i -> materials.set(i, StackedIngredient.fromNetwork(buffer)));
            var result = buffer.readItem();
            return new LightsaberRecipe(materials, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, LightsaberRecipe recipe) {
            buffer.writeInt(recipe.materials.size());
            recipe.materials.forEach(ingredient -> ingredient.toNetwork(buffer));
            buffer.writeItem(recipe.result);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final ItemStack result;
        private final Function<ItemLike, Criterion<?>> hasItem;
        private final Function<TagKey<Item>, Criterion<?>> hasTag;
        private final NonNullList<StackedIngredient> materials = NonNullList.create();
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        private RecipeCategory category = RecipeCategory.MISC;

        private Builder(ItemStack result, Function<ItemLike, Criterion<?>> hasItem, Function<TagKey<Item>, Criterion<?>> hasTag) {
            this.result = result;
            this.hasItem = hasItem;
            this.hasTag = hasTag;
        }

        public void requiresMaterial(Material<?> material) {
            this.materials.add(material.asStackedIngredient());
            this.unlockedBy("has_" + material.getName(), material.createTrigger(this.hasItem, this.hasTag));
        }

        @Override
        public @NotNull Builder unlockedBy(String name, Criterion<?> trigger) {
            this.criteria.put(name, trigger);
            return this;
        }

        @Override
        public @NotNull Builder group(@Nullable String group) {
            return this;
        }

        public Builder category(RecipeCategory category) {
            this.category = category;
            return this;
        }

        @Override
        public @NotNull Item getResult() {
            return this.result.getItem();
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation id) {
            this.validate(id);
            var builder = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(builder::addCriterion);
            output.accept(id, new LightsaberRecipe(this.materials, this.result), builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
        }

        private void validate(ResourceLocation id) {
            if(this.materials.isEmpty()) throw new IllegalArgumentException("There must be at least one material for workbench crafting recipe %s".formatted(id));
            if(this.criteria.isEmpty()) throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
