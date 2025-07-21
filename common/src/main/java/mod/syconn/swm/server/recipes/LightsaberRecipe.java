package mod.syconn.swm.server.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.utils.generic.JsonUtil;
import mod.syconn.swm.utils.server.StackedIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record LightsaberRecipe(ResourceLocation id, ItemStack item, ImmutableList<StackedIngredient> ingredients) implements Recipe<Container> {

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return item.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.LIGHTSABER_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.LIGHTSABER.get();
    }

    public static LightsaberRecipe getRecipe(Level level, ResourceLocation id) {
        return level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == ModRecipes.LIGHTSABER.get())
                .map(recipe -> (LightsaberRecipe) recipe)
                .filter(recipe -> recipe.getId().equals(id))
                .findFirst().orElse(null);
    }

    public static class Serializer implements RecipeSerializer<LightsaberRecipe> {

        @Override
        public @NotNull LightsaberRecipe fromJson(ResourceLocation pRecipeId, JsonObject parent) {
            var builder = ImmutableList.<StackedIngredient>builder();
            var input = GsonHelper.getAsJsonArray(parent, "materials");
            for (int i = 0; i < input.size(); i++) {
                JsonObject object = input.get(i).getAsJsonObject();
                builder.add(StackedIngredient.fromJson(object));
            }
            if (!parent.has("result")) throw new JsonSyntaxException("Missing result item entry");
            var resultObject = GsonHelper.getAsJsonObject(parent, "result");
            var resultItem = JsonUtil.getItemStack(resultObject, true);
            return new LightsaberRecipe(pRecipeId, resultItem, builder.build());
        }

        @Override
        public @NotNull LightsaberRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var result = buffer.readItem();
            var builder = ImmutableList.<StackedIngredient>builder();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) builder.add(StackedIngredient.fromNetwork(buffer));
            return new LightsaberRecipe(recipeId, result, builder.build());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, LightsaberRecipe recipe) {
            buffer.writeItem(recipe.item());
            buffer.writeVarInt(recipe.ingredients.size());
            for (StackedIngredient ingredient : recipe.ingredients) ingredient.toNetwork(buffer);
        }
    }
}
