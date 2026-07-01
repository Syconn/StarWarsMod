package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.server.recipes.LightsaberRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

@AutoRegister
public class ModRecipes {

    public static final RegistryEntry<RecipeType<LightsaberRecipe>> LIGHTSABER = RegistryEntry.recipeType("lightsabers");

    public static final RegistryEntry<LightsaberRecipe.Serializer> LIGHTSABER_SERIALIZER = RegistryEntry.recipeSerializer("lightsaber", LightsaberRecipe.Serializer::new);

    public static <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFromId(RecipeType<T> type, Level level, ResourceLocation id) {
        return level.getRecipeManager().getAllRecipesFor(type).stream().filter(r -> r.getId().equals(id)).findFirst();
    }
}
