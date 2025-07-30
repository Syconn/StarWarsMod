package mod.syconn.swm.features.lightsaber.client.screen;

import mod.syconn.swm.client.screen.components.ColoredSliderBar;
import mod.syconn.swm.client.screen.components.buttons.ColoredLightsaberButton;
import mod.syconn.swm.client.screen.components.buttons.ExpandedButton;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.server.menu.LightsaberWorkbenchMenu;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.ColorUtil;
import mod.syconn.swm.utils.generic.GraphicsUtil;
import mod.syconn.swm.utils.generic.MathUtil;
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

    private final ColoredSliderBar[] scrollBars = new ColoredSliderBar[3];
    private final ExpandedButton[] toggleButtons = new ExpandedButton[2];
    private ExpandedButton setAllButton;
    private UUID itemId;
    private double deltaScroll = 0;
    private float rotation = -45f;
    private int blade = 0;
    private float hue = 0, saturation = 0, value = 0;

    public LightsaberWorkbenchScreen(LightsaberWorkbenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 241;
    }

    @Override
    protected void init() {
        super.init();

        this.scrollBars[0] = this.addRenderableWidget(new ColoredSliderBar(this.leftPos + 47, this.topPos + 63, 161, 16, "", 0, 355, this.hue * 355f,
                f -> ColorUtil.packHsv((f * (355f / 161f)) / 360f, this.saturation, this.value), b -> this.hue = b.getValueInt() / 355f));
        this.scrollBars[1] = this.addRenderableWidget(new ColoredSliderBar(this.leftPos + 47, this.topPos + 83, 161, 16, "", 0, 100, this.saturation * 100f,
                f -> ColorUtil.packHsv(this.hue, f / 161f, this.value), b -> this.saturation = b.getValueInt() / 100f));
        this.scrollBars[2] = this.addRenderableWidget(new ColoredSliderBar(this.leftPos + 47, this.topPos + 103, 161, 16, "", 0, 100, this.value * 100f,
                f -> ColorUtil.packHsv(this.hue, this.saturation, f / 161f), b -> this.value = b.getValueInt() / 100f));

        this.toggleButtons[0] = this.addRenderableWidget(new ExpandedButton(this.leftPos + 5, this.topPos + 83, 20, 20, "<", b -> changeBlade(-1)));
        this.toggleButtons[1] = this.addRenderableWidget(new ExpandedButton(this.leftPos + 25, this.topPos + 83, 20, 20, ">", b -> changeBlade(1)));
        this.setAllButton = this.addRenderableWidget(new ExpandedButton(this.leftPos + 5, this.topPos + 103, 40, 20, "Set All", b -> updateLightsaberColor(true)));

        getLightsaberColor();

        this.addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 46, this.topPos + 122, "", BLUE, 0, 0, this::updateColorButton));
        this.addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 74, this.topPos + 122, "", GREEN, 1, 0, this::updateColorButton));
        this.addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 102, this.topPos + 122, "", YELLOW, 2, 1, this::updateColorButton));
        this.addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 135, this.topPos + 122, "", WHITE, 1, 1, this::updateColorButton));
        this.addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 163, this.topPos + 122, "", PURPLE, 2, 0, this::updateColorButton));
        this.addRenderableWidget(new ColoredLightsaberButton(this.leftPos + 191, this.topPos + 122, "", RED, 0, 1, this::updateColorButton));
    }

    private void updateColorButton(Button button) {
        this.setColor(((ColoredLightsaberButton) button).getHSV());
    }

    private void changeBlade(int direction) {
        final var stack = getMenu().getBlockEntity().getContainer().getItem(0);
        if (stack.getItem() instanceof LightsaberItem) this.blade = MathUtil.wrap(this.blade + direction, LightsaberTag.getOrCreate(stack).blades.size() - 1);
        getLightsaberColor();
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

        final var stack = this.menu.getBlockEntity().getContainer().getItem(0);
        if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
            final var lT = LightsaberTag.getOrCreate(stack);
            final var renderStack = lT.getTemporary(this.blade, 1.0f);
            this.rotation += (float) (-10f * this.deltaScroll);
            GraphicsUtil.renderLightsaberFromBehind(guiGraphics, renderStack, this.leftPos + 247, this.topPos + 36.5, this.rotation, this.blade);
            this.deltaScroll = 0f;

            if (!lT.uuid.equals(this.itemId)) {
                getLightsaberColor();
                this.blade = 0;
            }
            else if (lT.getColor(this.blade) != ColorUtil.packHsv(this.hue, this.saturation, this.value)) updateLightsaberColor(false);
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

    @Override
    protected void containerTick() {
        super.containerTick();

        final var stack = this.menu.getBlockEntity().getContainer().getItem(0);
        if (LightsaberTag.getOrCreate(stack).blades.size() > 1) {
            this.toggleButtons[0].visible = true;
            this.toggleButtons[1].visible = true;
            this.setAllButton.visible = true;
        } else {
            this.toggleButtons[0].visible = false;
            this.toggleButtons[1].visible = false;
            this.setAllButton.visible = false;
        }
    }

    private void getLightsaberColor() {
        final var stack = getMenu().getBlockEntity().getContainer().getItem(0);
        if (stack.getItem() instanceof LightsaberItem) {
            var lT = LightsaberTag.getOrCreate(stack);
            setColor(lT.getColor(this.blade));
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

    private void updateLightsaberColor(boolean allBlades) {
        Network.CHANNEL.sendToServer(new ChangeLightsaberHSVPacket(this.menu.getBlockEntity().getBlockPos(), ColorUtil.packHsv(this.hue, this.saturation, this.value), allBlades ? -1 : this.blade));
    }
}
