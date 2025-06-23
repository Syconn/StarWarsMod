package mod.syconn.swm.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.server.recipes.LightsaberRecipe;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(Constants.MOD, Registries.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(Constants.MOD, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeType<LightsaberRecipe>> LIGHTSABER = registerRecipe("lightsaber");

    public static final RegistrySupplier<LightsaberRecipe.Serializer> LIGHTSABER_SERIALIZER = SERIALIZER.register("lightsaber", LightsaberRecipe.Serializer::new);

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> registerRecipe(String name) {
        return RECIPES.register(name, () -> new RecipeType<>() {
            public String toString() { return name; }
        });
    }
}
