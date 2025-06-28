package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.util.client.FontUtil;
import mod.syconn.swm.util.client.GraphicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

public class ExpandedButton extends Button {

    protected static final WidgetSprites SPRITES = new WidgetSprites(
            new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted")
    );

    public ExpandedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
        this(xPos, yPos, width, height, displayString, handler, DEFAULT_NARRATION);
    }

    public ExpandedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, CreateNarration createNarration) {
        super(xPos, yPos, width, height, displayString, handler, createNarration);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int k = !this.active ? 0 : (this.isHovered ? 2 : 1);
        guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());

        final FormattedText buttonText = FontUtil.ellipsize(mc.font, this.getMessage(), this.width - 6);
        guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
    }

    protected int getFGColor() {
        return this.active ? 16777215 : 10526880;
    }
}
