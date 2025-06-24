package mod.syconn.swm.fabric;

import mod.syconn.swm.StarWars;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.fabricmc.fabric.mixin.recipe.ingredient.IngredientMixin;
import net.minecraft.world.item.crafting.Ingredient;

public final class StarWarsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StarWars.init();
    }
}
