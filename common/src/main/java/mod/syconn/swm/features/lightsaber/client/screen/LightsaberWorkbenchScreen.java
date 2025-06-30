package mod.syconn.swm.features.lightsaber.client.screen;

import mod.syconn.swm.client.screen.components.ColoredLightsaberButton;
import mod.syconn.swm.client.screen.components.ColoredScrollBar;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.server.container.LightsaberWorkbenchMenu;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.GraphicsUtil;
import mod.syconn.swm.utils.math.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.UUID;

import static mod.syconn.swm.features.addons.LightsaberContent.*;

@Environment(EnvType.CLIENT)
public class LightsaberWorkbenchScreen extends AbstractContainerScreen<LightsaberWorkbenchMenu> {

    private static final ResourceLocation WORKSTATION_BACKGROUND = Constants.withId("textures/gui/lightsaber_workbench.png");

    private final ColoredScrollBar[] scrollBars = new ColoredScrollBar[3];
    private UUID itemId;
    private double deltaScroll = 0;
    private float rotation = -45f;
    private float hue = 0, saturation = 0, value = 0;

    public LightsaberWorkbenchScreen(LightsaberWorkbenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 241;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(this.scrollBars[0] = new ColoredScrollBar(this.leftPos + 47, this.topPos + 63, 161, 16, "", 0, 355, this.hue * 355f,
                f -> ColorUtil.packHsv((f * (355f / 161f)) / 360f, this.saturation, this.value), b -> this.hue = b.getValueInt() / 355f));
        addRenderableWidget(this.scrollBars[1] = new ColoredScrollBar(this.leftPos + 47, this.topPos + 83, 161, 16, "", 0, 100, this.saturation * 100f,
                f -> ColorUtil.packHsv(this.hue, f / 161f, this.value), b -> this.saturation = b.getValueInt() / 100f));
        addRenderableWidget(this.scrollBars[2] = new ColoredScrollBar(this.leftPos + 47, this.topPos + 103, 161, 16, "", 0, 100, this.value * 100f,
                f -> ColorUtil.packHsv(this.hue, this.saturation, f / 161f), b -> this.value = b.getValueInt() / 100f));

        getLightsaberColor();

        addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 46, this.topPos + 122, "", BLUE, 0, 0, this::updateColorButton));
        addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 74, this.topPos + 122, "", GREEN, 1, 0, this::updateColorButton));
        addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 102, this.topPos + 122, "", YELLOW, 2, 1, this::updateColorButton));
        addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 135, this.topPos + 122, "", WHITE, 1, 1, this::updateColorButton));
        addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 163, this.topPos + 122, "", PURPLE, 2, 0, this::updateColorButton));
        addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 191, this.topPos + 122, "", RED, 0, 1, this::updateColorButton));
    }

    private void updateColorButton(Button button) {
        this.setColor(((ColoredLightsaberButton) button).getHSV());
    }

    @Override
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

        var stack = this.menu.getBlockEntity().getContainer().getItem(0);
        if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
            var lT = LightsaberTag.getOrCreate(stack);
            this.rotation += (float) (-10f * this.deltaScroll);
            GraphicsUtil.renderLightsaber(guiGraphics, lT.getTemporary(true, true), this.leftPos + 185, this.topPos + 36.5, this.rotation);
            this.deltaScroll = 0f;

            if (!lT.uuid.equals(this.itemId)) getLightsaberColor();
            else if (lT.color != ColorUtil.packHsv(this.hue, this.saturation, this.value)) updateLightsaberColor(lT);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (var bar : this.scrollBars) if (bar != null && bar.isMouseOver(mouseX, mouseY)) bar.onDrag(mouseX, mouseY, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) { }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.deltaScroll = delta;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void getLightsaberColor() {
        var stack = getMenu().getBlockEntity().getContainer().getItem(0);
        if (stack.getItem() instanceof LightsaberItem) {
            var lT = LightsaberTag.getOrCreate(stack);
            setColor(lT.color);
            this.itemId = lT.uuid;
        }
    }

    private void setColor(int color) {
        this.hue = ColorUtil.hsvGetH(color);
        this.saturation = ColorUtil.hsvGetS(color);
        this.value = ColorUtil.hsvGetV(color);

        this.scrollBars[0].setValue(this.hue * 355f);
        this.scrollBars[1].setValue(this.saturation * 100f);
        this.scrollBars[2].setValue(this.value * 100f);
    }

    private void updateLightsaberColor(LightsaberTag lT) {
        lT.color = ColorUtil.packHsv(this.hue, this.saturation, this.value);
        Network.CHANNEL.sendToServer(new ChangeLightsaberHSVPacket(this.menu.getBlockEntity().getBlockPos(), lT.color));
    }
}
