package mod.syconn.swm.features.lightsaber.client.screen;

import mod.syconn.swm.client.screen.components.ExpandedButton;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.network.CraftHiltPacket;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberAssemblerMenu;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.client.GraphicsUtil;
import mod.syconn.swm.util.client.render.IngredientRenderer;
import mod.syconn.swm.util.math.MathUtil;
import mod.syconn.swm.util.server.StackedIngredient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class LightsaberAssemblerScreen extends AbstractContainerScreen<LightsaberAssemblerMenu> {

    private static final ResourceLocation WORKSTATION_BACKGROUND = Constants.withId("textures/gui/lightsaber_assembler.png");

    private final List<IngredientRenderer<StackedIngredient>> ingredientRenderers = new ArrayList<>();
    private final Inventory playerInventory;
    private double deltaScroll = 0;
    private float rotation = -45f;
    private ExpandedButton craftButton;
    private int selectedRecipe;

    public LightsaberAssemblerScreen(LightsaberAssemblerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.playerInventory = playerInventory;
        this.imageWidth = 198;
        this.imageHeight = 184;
    }

    @Override
    protected void init() { // TODO RENDER TEXT OF BLADE NAME, ROTATE OTHER WAYS WITH MOUSE, ZOOM MAYBE
        createRenderers();
        super.init();
        addRenderableWidget(new ExpandedButton(this.leftPos + 8, this.topPos + 53, 20, 20, Component.literal("<"), pButton -> {
            this.selectedRecipe = MathUtil.wrap(this.selectedRecipe - 1, this.menu.getRecipes().size() - 1);
            createRenderers();
        }));

        addRenderableWidget(new ExpandedButton(this.leftPos + 148, this.topPos + 53, 20, 20, Component.literal(">"), pButton -> {
            this.selectedRecipe = MathUtil.wrap(this.selectedRecipe + 1, this.menu.getRecipes().size() - 1);
            createRenderers();
        }));

        addRenderableWidget(this.craftButton = new ExpandedButton(this.leftPos + 131, this.topPos + 78, 36, 18, Component.literal("Craft"), pButton -> {
            Network.CHANNEL.sendToServer(new CraftHiltPacket(this.menu.getBlockEntity().getBlockPos(), this.menu.getRecipes().get(this.selectedRecipe).getId()));
        }));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(WORKSTATION_BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        var lT = LightsaberTag.getOrCreate(this.menu.getRecipes().get(selectedRecipe).item());
        this.rotation += (float) (-10f * this.deltaScroll);
        GraphicsUtil.renderLightsaber(guiGraphics, lT.getTemporary(false, false), this.leftPos + 80, this.topPos + 36.5, this.rotation);
        this.deltaScroll = 0f;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.craftButton.visible = renderSlots(guiGraphics, mouseX, mouseY);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void containerTick() {
        this.ingredientRenderers.forEach(IngredientRenderer::tick);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.deltaScroll = delta;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private boolean renderSlots(GuiGraphics graphics, int mouseX, int mouseY){
        var canCraft = true;

        for (int i = 0; i < this.ingredientRenderers.size(); i++){
            var ingredient = this.ingredientRenderers.get(i);
            var amountNeeded = ingredient.getIngredient().getCount();
            var playerAmount = 0;

            for (int pI = 0; pI < this.playerInventory.items.size(); pI++)
                if (ingredient.getIngredient().test(this.playerInventory.items.get(pI)))
                    playerAmount += this.playerInventory.items.get(pI).getCount();

            amountNeeded -= playerAmount;
            if (amountNeeded > 0) canCraft = false;

            if (this.minecraft != null) {
                var x = this.leftPos + 11 + (27 * i);
                var y = this.topPos + 78;
                graphics.blit(WORKSTATION_BACKGROUND, x, y, 198, amountNeeded <= 0 ? 20 : 38, 18, 18);
                ingredient.display(graphics, amountNeeded,this.leftPos + 12 + (27 * i), this.topPos + 79, mouseX, mouseY);
            }
        }

        return canCraft;
    }

    public void createRenderers() {
        this.ingredientRenderers.clear();
        this.menu.getRecipes().get(this.selectedRecipe).ingredients().forEach(s -> this.ingredientRenderers.add(new IngredientRenderer<>(s)));
    }
}
