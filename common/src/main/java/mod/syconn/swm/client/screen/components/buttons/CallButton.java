package mod.syconn.swm.client.screen.components.buttons;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.interfaces.IWidgetComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CallButton extends ExpandedButton implements IWidgetComponent {

    private static final ResourceLocation HOLOGRAM_SCREEN = Constants.withId("textures/gui/hologram_screen.png");
    private final float scalar;
    private final Type type;

    public CallButton(int xPos, int yPos, Type type, String hover, OnPress handler) {
        this(xPos, yPos, 1.0f, type, hover, handler);
    }

    public CallButton(int xPos, int yPos, float scalar, Type type, String hover, OnPress handler) {
        super(xPos, yPos, (int)(20 * scalar), (int)(20 * scalar), Component.literal(hover), handler);
        this.type = type;
        this.scalar = scalar;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();

        var uX = type == Type.START ? 0 : 20;
        var uY = isHovered ? 58 : 38;
        guiGraphics.pose().scale(this.scalar, this.scalar, this.scalar);
        guiGraphics.blit(HOLOGRAM_SCREEN, (int) (this.getX() * (1 / this.scalar)), (int) (this.getY() * (1 / this.scalar)), uX, uY, 20, 20);

        guiGraphics.pose().popPose();

        if (isHovered) guiGraphics.renderTooltip(GameInstance.getClient().font, this.getMessage(), mouseX, mouseY);
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        START,
        END
    }
}
