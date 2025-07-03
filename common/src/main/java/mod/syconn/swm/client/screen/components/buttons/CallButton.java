package mod.syconn.swm.client.screen.components.buttons;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.utils.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

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

        if (isHovered) guiGraphics.renderTooltip(GameInstance.getClient().font, createNarrationMessage(), mouseX, mouseY);
    }

    @Override
    protected @NotNull MutableComponent createNarrationMessage() {
        return Component.literal(type == Type.START ? "Start Call" : "End Call");
    }

    public enum Type {
        START,
        END
    }
}
