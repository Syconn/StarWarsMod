package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.utils.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ColoredLightsaberButton extends ExpandedButton {

    private static final ResourceLocation COMPONENTS = Constants.withId("textures/gui/components.png");

    private final int hsv;
    private final int u, v;

    public ColoredLightsaberButton(int xPos, int yPos, String displayString, int hsv, int u, int v, OnPress handler) {
        this(xPos, yPos, Component.literal(displayString), hsv, u, v, handler);
    }

    public ColoredLightsaberButton(int xPos, int yPos, Component displayString, int hsv, int u, int v, OnPress handler) {
        super(xPos, yPos, 18, 18, displayString, handler);
        this.hsv = hsv;
        this.u = u;
        this.v = v;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();

        guiGraphics.pose().scale(0.25f, 0.25f, 0.25f);
        guiGraphics.blit(COMPONENTS, getX() * 4, getY() * 4, 71 * u, 24 + 71 * v, 71, 71);

        guiGraphics.pose().popPose();
    }

    public int getHSV() {
        return hsv;
    }
}
