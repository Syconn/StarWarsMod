package mod.syconn.swm.features.lightsaber.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.math.Axis;
import mod.syconn.swm.client.screen.components.ColoredScrollBar;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberWorkbenchMenu;
import mod.syconn.swm.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class LightsaberWorkbenchScreen extends AbstractContainerScreen<LightsaberWorkbenchMenu> {

    private static final ResourceLocation WORKSTATION_BACKGROUND = Constants.withId("textures/gui/lightsaber_workbench.png");

    private final ColoredScrollBar[] scrollBars = new ColoredScrollBar[3];
    private double deltaScroll = 0f;

    public LightsaberWorkbenchScreen(LightsaberWorkbenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 241;
    }

    protected void init() {
        super.init();

        addRenderableWidget(scrollBars[0] = new ColoredScrollBar(this.leftPos + 47, this.topPos + 63, 161, 16, "", 0, 355, 185, false));
        addRenderableWidget(scrollBars[1] = new ColoredScrollBar(this.leftPos + 47, this.topPos + 83, 161, 16, "", 0, 100, 100, false));
        addRenderableWidget(scrollBars[2] = new ColoredScrollBar(this.leftPos + 47, this.topPos + 103, 161, 16, "", 0, 100, 100, false));
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(WORKSTATION_BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        renderLightsaber(guiGraphics);
    }

    public void renderLightsaber(GuiGraphics guiGraphics) {
        var i = (this.width - this.imageWidth) / 2;
        var j = (this.height - this.imageHeight) / 2;
        var level = this.minecraft.level;
        var stack = LightsaberTag.getTemporary(getMenu().getBlockEntity().getContainer().getItem(0).copy(), false);
        var lT = LightsaberTag.getOrCreate(stack);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(i + 185, j + 36.5, 50.0);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(90f));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-45f));
        guiGraphics.pose().scale(100, 100, 100);
        Lighting.setupFor3DItems();

        if (!getMenu().getBlockEntity().getContainer().getItem(0).isEmpty()) {
            var model = this.minecraft.getItemRenderer().getModel(stack, level, this.minecraft.player, 0);
            if (!model.usesBlockLight()) Lighting.setupForFlatItems();
            Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, model);
            if (!model.usesBlockLight()) Lighting.setupFor3DItems();
        }
        guiGraphics.flush();
        guiGraphics.pose().popPose();
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (var bar : scrollBars) if (bar.isMouseOver(mouseX, mouseY)) bar.onDrag(mouseX, mouseY, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) { }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.deltaScroll = delta;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
