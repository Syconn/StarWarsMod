package mod.syconn.swm.features.lightsaber.client.screen;

import mod.syconn.swm.features.lightsaber.server.container.LightsaberAssemblerMenu;
import mod.syconn.swm.util.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LightsaberAssemblerScreen extends AbstractContainerScreen<LightsaberAssemblerMenu> {

    private static final ResourceLocation WORKSTATION_BACKGROUND = Constants.withId("textures/gui/lightsaber_assembler.png");

    public LightsaberAssemblerScreen(LightsaberAssemblerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 198;
        this.imageHeight = 184;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(WORKSTATION_BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}
}
