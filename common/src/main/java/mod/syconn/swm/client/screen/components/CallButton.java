package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.utils.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CallButton extends ExpandedButton {

    private static final ResourceLocation HOLOGRAM_SCREEN = Constants.withId("textures/gui/hologram_screen.png");
    private final Type type;

    public CallButton(int xPos, int yPos, Type type, OnPress handler) {
        super(xPos, yPos, 20, 20, Component.empty(), handler);
        this.type = type;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var uX = type == Type.START ? 0 : 20;
        var uY = isHovered ? 58 : 38;
        guiGraphics.blit(HOLOGRAM_SCREEN, this.getX(), this.getY(), uX, uY, width, height);
    }

    public enum Type {
        START,
        END
    }
}
