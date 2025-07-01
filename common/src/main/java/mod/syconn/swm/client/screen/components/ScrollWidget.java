package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.GraphicsUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class ScrollWidget extends AbstractWidget {

    private static final ResourceLocation HOLOGRAM_SCREEN = Constants.withId("textures/gui/hologram_screen.png");
    private final Function<ScrollWidget, Boolean> canScroll;
    private final int size;
    private float scrollOffs;

    public ScrollWidget(int x, int y, int height, int size, Function<ScrollWidget, Boolean> canScroll) {
        super(x, y, 14, height + 2, Component.empty());
        this.canScroll = canScroll;
        this.size = size;
    }

    private int getTextureY() {
        return this.isHovered ? 84 : 96;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GraphicsUtil.blitSliced(guiGraphics, HOLOGRAM_SCREEN, this.getX(), this.getY(), height + 2, 14, 1, 84, 53);
        guiGraphics.blit(HOLOGRAM_SCREEN, this.getX() + 1, (int) (this.getY() + 1 + height * this.scrollOffs), this.getTextureY(), 38, 12, 15);
        // guiGraphics.blit(CREATIVE_TABS_LOCATION, i, j + (int)((k - j - 17) * this.scrollOffs), 232 + (this.canScroll() ? 0 : 12), 0, 12, 15);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.canScroll.apply(this)) return false;
        else {
            this.scrollOffs = this.subtractInputFromScroll(this.scrollOffs, delta);
//            this.menu.scrollTo(this.scrollOffs);
            return true;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.canScroll.apply(this)) {
            int i = getY() + 18;
            int j = i + 112;
            this.scrollOffs = ((float)mouseY - i - 7.5F) / (j - i - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
//            this.menu.scrollTo(this.scrollOffs);
            return true;
        } else return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    protected int calculateRowCount() {
        return Mth.positiveCeilDiv(size, 9) - 5;
    }
//
//    protected int getRowIndexForScroll(float scrollOffs) {
//        return Math.max((int)(scrollOffs * this.calculateRowCount() + 0.5), 0);
//    }
//
//    protected float getScrollForRowIndex(int rowIndex) {
//        return Mth.clamp((float)rowIndex / this.calculateRowCount(), 0.0F, 1.0F);
//    }
//
    protected float subtractInputFromScroll(float scrollOffs, double input) {
        return Mth.clamp(scrollOffs - (float)(input / this.calculateRowCount()), 0.0F, 1.0F);
    }
}
