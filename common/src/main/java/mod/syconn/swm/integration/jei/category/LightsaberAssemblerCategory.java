package mod.syconn.swm.integration.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.server.recipes.LightsaberRecipe;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.client.GraphicsUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LightsaberAssemblerCategory implements IRecipeCategory<LightsaberRecipe> {

    public static final RecipeType<LightsaberRecipe> LIGHTSABER = RecipeType.create(Constants.MOD, "lightsaber", LightsaberRecipe.class);

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Constants.MOD, "textures/gui/lightsaber_assembler.png");
    private static final String TITLE_KEY = "Lightsaber Assembler";
    private static final String MATERIALS_KEY = "Materials";

    private final IDrawableStatic background;
    private final IDrawableStatic window;
    private final IDrawableStatic inventory;
    private final IDrawable icon;
    private final Component title;

    public LightsaberAssemblerCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(162, 124);
        this.window = helper.createDrawable(BACKGROUND, 7, 15, 162, 72);
        this.inventory = helper.createDrawable(BACKGROUND, 7, 101, 162, 36);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.LIGHTSABER_WORKBENCH.get()));
        this.title = Component.translatable(TITLE_KEY);
    }

    @Override
    public @NotNull RecipeType<LightsaberRecipe> getRecipeType() {
        return LIGHTSABER;
    }

    @Override
    public @NotNull Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, LightsaberRecipe recipe, @NotNull IFocusGroup focuses) {
        ItemStack output = recipe.item().copy();
        for(int i = 0; i < recipe.ingredients().size(); i++) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack mat : recipe.ingredients().get(i).ingredient().getItems()) stacks.add(new ItemStack(mat.getItem(), recipe.ingredients().get(i).count()));
            builder.addSlot(RecipeIngredientRole.INPUT, (i % 8) * 18 + 1, 88 + (i / 8) * 18).addItemStacks(stacks);
        }
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(output);
    }

    @Override
    public void draw(LightsaberRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.window.draw(guiGraphics, 0, 0);
        this.inventory.draw(guiGraphics, 0, this.window.getHeight() + 2 + 11 + 2);

        guiGraphics.drawString(GameInstance.getClient().font, I18n.get(MATERIALS_KEY), 0, 78, 0x7E7E7E);
        ItemStack output = recipe.item();
        int titleX = this.window.getWidth() / 2;
        guiGraphics.drawCenteredString(GameInstance.getClient().font, output.getHoverName().getString(), titleX, 5, 0xFFFFFFFF);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.mulPoseMatrix(poseStack.last().pose());
//        poseStack.translate(81, 40, 0);
//        poseStack.scale(40F, 40F, 40F);
//        poseStack.mulPose(Axis.XP.rotationDegrees(-5F));
//        if (GameInstance.getClient().player != null) poseStack.mulPose(Axis.YP.rotationDegrees(GameInstance.getClient().player.tickCount + partialTicks));
//        poseStack.scale(-1, -1, -1);
//        RenderSystem.applyModelViewMatrix();

        GraphicsUtil.renderLightsaber(guiGraphics, output, 81, 40, -45f);

        poseStack.popPose();
//        RenderSystem.applyModelViewMatrix();
    }
}