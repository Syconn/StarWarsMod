package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.utils.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CallButton extends ExpandedButton {

    private static final ResourceLocation HOLOGRAM_SCREEN = Constants.withId("textures/gui/hologram_screen.png");
    private final Type type;

    public CallButton(int xPos, int yPos, Type type, OnPress handler) {
        super(xPos, yPos, (int)(20 * 0.80f), (int)(20 * 0.80f), Component.empty(), handler);
        this.type = type;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();

        var uX = type == Type.START ? 0 : 20;
        var uY = isHovered ? 58 : 38;
        guiGraphics.pose().scale(0.80f, 0.80f, 0.80f);
        guiGraphics.blit(HOLOGRAM_SCREEN, this.getX() * 5 / 4, this.getY() * 5 / 4, uX, uY, 20, 20);

        guiGraphics.pose().popPose();
    }

    public enum Type {
        START,
        END
    }
}
