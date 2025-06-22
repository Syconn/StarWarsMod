package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.client.GraphicsUtil;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class ColoredButton extends ExpandedButton {

    private static final ResourceLocation COMPONENTS = Constants.withId("textures/gui/components.png");

    private final int hsv;

    public ColoredButton(int xPos, int yPos, Component displayString, int hsv, OnPress handler) {
        super(xPos, yPos, 18, 18, displayString, handler);
        this.hsv = hsv;
    }

    public ColoredButton(int xPos, int yPos, String displayString, int hsv, OnPress handler) {
        super(xPos, yPos, 18, 18, Component.literal(displayString), handler);
        this.hsv = hsv;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(COMPONENTS, getX(), getY(), 25, 0, this.width, this.height);

        var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(hsv), ColorUtil.hsvGetS(hsv), 1.0F);
        GraphicsUtil.fillRect(guiGraphics, getX() + 2, getY() + 2, 14, 14, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 255);

        //BOARDERS
        GraphicsUtil.fillRect(guiGraphics, getX() + 2, getY() + 1, 14, 1, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 100);
        GraphicsUtil.fillRect(guiGraphics, getX() + 1, getY() + 2, 1, 14, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 100);
        GraphicsUtil.fillRect(guiGraphics, getX() + 16, getY() + 1, 1, 14, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 100);
        GraphicsUtil.fillRect(guiGraphics, getX() + 1, getY() + 16, 14, 1, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 100);
    }

    public int getHSV() {
        return hsv;
    }
}
