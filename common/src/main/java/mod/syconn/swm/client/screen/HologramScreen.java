package mod.syconn.swm.client.screen;

import mod.syconn.swm.client.screen.components.ErrorWidget;
import mod.syconn.swm.client.screen.components.buttons.CallButton;
import mod.syconn.swm.client.screen.components.buttons.ExpandedButton;
import mod.syconn.swm.client.screen.components.buttons.RefreshButton;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.serverside.HoloCallPacket;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.general.ListUtil;
import mod.syconn.swm.utils.block.WorldPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HologramScreen extends Screen {

    private static final ResourceLocation HOLOGRAM_SCREEN = Constants.withId("textures/gui/hologram_screen.png");
    private final boolean handheld;
    private final WorldPos worldPos;
    private Page page = Page.CREATE_CALL;
    private String lastSearch = "";
    private CallMenuWidget callData;
    private EditBox searchBox;
    private CallButton callButton;
    private ErrorWidget errorWidget;
    private Component pageTitle;

    public HologramScreen(WorldPos worldPos, boolean handheld) {
        super(Component.literal("Hologram Projector Screen"));
        this.handheld = handheld;
        this.worldPos = worldPos;
    }

    private int marginX() {
        return (this.width - 238) / 2;
    }

    @Override
    public void tick() {
        super.tick();
        this.searchBox.tick();
    }

    @Override
    protected void init() {
        var leftPos = (this.width - 236) / 2;
        var buttonSize = 220 / 3;

        this.pageTitle = Component.literal("Create Call");

        this.addRenderableWidget(new ExpandedButton(leftPos + 10, 44, buttonSize, 20, Component.literal("Create Call"), button -> this.showPage(Page.CREATE_CALL)));
        this.addRenderableWidget(new ExpandedButton(leftPos + 157, 44, buttonSize, 20, Component.literal("Join Call"), button -> this.showPage(Page.JOIN_CALL)));

        this.errorWidget = this.addRenderableWidget(new ErrorWidget(leftPos + 236 / 2, 20));

        var m = this.marginX() + 3;
        this.callButton = this.addRenderableWidget(new CallButton(m + 209, 74, 0.80f, CallButton.Type.START, "Start Call", this::createCall));
        this.callData = new CallMenuWidget(this, leftPos + 10, 92, this.page, this::addRenderableWidget);
        this.addRenderableWidget(new RefreshButton(m + 11, 90, this.callData::refresh));

        var string = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.font, this.marginX() + 29, 75, 178, 13, Component.literal(string));
        this.searchBox.setMaxLength(26);
        this.searchBox.setHint(Component.literal("Search"));
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.searchBox.setValue(string);
        this.searchBox.setResponder(this::checkSearchStringUpdate);
        this.addWidget(this.searchBox);
        this.showPage(this.page);
    }

    private void showPage(Page page) {
        this.page = page;
        this.callData.setPage(this.page);
        switch (page) {
            case CREATE_CALL:
                this.callButton.visible = true;
                this.pageTitle = Component.literal("Start Call");
                break;
            case JOIN_CALL:
                this.callButton.visible = false;
                this.pageTitle = Component.literal("Join Call");
                break;
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        super.renderBackground(guiGraphics);

        var m = this.marginX() + 3;
        guiGraphics.blitNineSliced(HOLOGRAM_SCREEN, m, 64, 236, 143, 8, 236, 34, 1, 1);
        guiGraphics.blit(HOLOGRAM_SCREEN, m + 10, 75, 243, 1, 12, 12);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var leftPos = (this.width - 236) / 2;

        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.minecraft.font, this.pageTitle, leftPos + 119, 50, -1);

        this.searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.callData.mouseScrolled(mouseX, mouseY, delta) || super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height); // TODO MAYBE ADD RESIZE SUPPORT
    }

    public void hologramData(HologramNetwork network) {
        this.callData.handleNetworkPacket(network);
    }

    private void checkSearchStringUpdate(String newText) {
        newText = newText.toLowerCase(Locale.ROOT);
        if (!newText.equals(this.lastSearch)) {
            this.lastSearch = newText;
            this.showPage(this.page);
            this.callData.search(newText);
        }
    }

    private void createCall(Button button) {
        if (!this.callData.getCallers().isEmpty()) {
            Network.CHANNEL.sendToServer(new HoloCallPacket(HoloCallPacket.Type.CREATE, UUID.randomUUID(), ListUtil.append(getCaller(), this.callData.getCallers())));
            Minecraft.getInstance().setScreen(null);
        } else this.errorWidget.displayError("ERROR: You must add at least one player", 100);
    }

    public void joinCall(UUID callId) { // TODO TEST
        Network.CHANNEL.sendToServer(new HoloCallPacket(HoloCallPacket.Type.CONNECT, callId, List.of(getCaller())));
        Minecraft.getInstance().setScreen(null);
    }

    public void leaveCall(UUID callId) { // TODO TEST
        Network.CHANNEL.sendToServer(new HoloCallPacket(HoloCallPacket.Type.LEAVE, callId, List.of(getCaller())));
    }

    private HologramNetwork.Caller getCaller() {
        return new HologramNetwork.Caller(this.minecraft.player.getUUID(), this.worldPos, this.handheld);
    }

    @Environment(EnvType.CLIENT)
    public enum Page {
        CREATE_CALL,
        JOIN_CALL
    }
}
