package mod.syconn.swm.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.integration.jei.category.LightsaberAssemblerCategory;
import mod.syconn.swm.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class Plugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return Constants.withId("crafting");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.LIGHTSABER.get(), (stack, context) -> String.valueOf(LightsaberTag.getOrCreate(stack).model));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new LightsaberAssemblerCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
        registration.addRecipes(LightsaberAssemblerCategory.LIGHTSABER, getRecipes(ModRecipes.LIGHTSABER.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.LIGHTSABER_WORKBENCH.get()), LightsaberAssemblerCategory.LIGHTSABER);
    }

    private <C extends Container, T extends Recipe<C>> List<T> getRecipes(RecipeType<T> type) {
        return getRecipeManager().getAllRecipesFor(type).stream().map(RecipeHolder::value).toList();
    }

    public static RecipeManager getRecipeManager() {
        ClientPacketListener listener = Objects.requireNonNull(Minecraft.getInstance().getConnection());
        return listener.getRecipeManager();
    }
}
