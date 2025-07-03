package mod.syconn.swm.utils.client.render;

import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.client.screen.HologramScreen;
import mod.syconn.swm.client.screen.components.ScrollWidget;
import mod.syconn.swm.client.screen.components.buttons.ToggleButton;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.serverside.RequestHologramPacket;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.client.GraphicsUtil;
import mod.syconn.swm.utils.client.WidgetComponent;
import mod.syconn.swm.utils.math.ColorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class CallDataRenderer implements WidgetComponent {

    private final List<CreateCallData> listedCreateCallPlayers = new ArrayList<>();
    private final List<CreateCallData> shownCreateCallPlayers = new ArrayList<>();
    private final List<JoinCallData> listedJoinCallPlayers = new ArrayList<>();
    private final List<JoinCallData> shownJoinCallPlayers = new ArrayList<>();
    private final ToggleButton[] toggleButtons = new ToggleButton[3];
    private final Minecraft minecraft = Minecraft.getInstance();
    private final int color = ColorUtil.packArgb(74, 74, 74, 255);
    private final int height = 32, width = 220;
    private final int x, y;
    private ScrollWidget scroller;
    private HologramScreen.Page page;
    private String lastSearch;
    private int scroll = 0;

    public CallDataRenderer(int x, int y, HologramScreen.Page page, Function<WidgetComponent, WidgetComponent> widgets) {
        this.x = x;
        this.y = y;
        this.page = page;

        this.refreshPlayerList();
        this.init(widgets);
        this.updateMenu(this.scroll);
        this.search(this.lastSearch);
    }

    private void init(Function<WidgetComponent, WidgetComponent> widgets) {
        widgets.apply(this);

        for (int i = 0; i < 3; i++) {
            final var v = i;
            this.toggleButtons[v] = (ToggleButton) widgets.apply(new ToggleButton(this.x + 180, this.y + 21 + this.height * v, false, ToggleButton.Color.GREEN, b -> this.toggled(b, v)));
        }

        this.scroller = (ScrollWidget) widgets.apply(new ScrollWidget(x + 207, y + 11, 91, this.listedCreateCallPlayers.size() - 3, w -> true, this::updateMenu));
    }

    private void toggled(ToggleButton button, int i) {
        var player = this.listedCreateCallPlayers.get(this.scroll + i);
        this.listedCreateCallPlayers.set(this.scroll + i, new CreateCallData(player.info, button.isActive(), player.locked));
    }

    private void refreshPlayerList() {
        if (this.minecraft.player != null) {
            this.listedCreateCallPlayers.clear();

            if (this.page == HologramScreen.Page.CREATE_CALL) {
                var connection = this.minecraft.player.connection;
                var uuids = connection.getOnlinePlayerIds();
                uuids.forEach(uuid -> this.listedCreateCallPlayers.add(CreateCallData.of(connection.getPlayerInfo(uuid), isPlayerMe(connection.getPlayerInfo(uuid)))));
                for (int i = 0; i < 2; i++) this.listedCreateCallPlayers.add(CreateCallData.of(ClientHooks.getInfo(ClientHooks.createMockPlayer(this.minecraft.level, "Test-Player" + (i + 1))), false)); // TODO REMOVE
            } else Network.CHANNEL.sendToServer(new RequestHologramPacket());
        }
    }

    private void updateMenu(int scroll) {
        this.scroll = scroll;
        this.scroller.updateSize(this.shownCreateCallPlayers.size() - 3);

        Arrays.stream(this.toggleButtons).forEach(b -> b.visible = false);

        if (this.page == HologramScreen.Page.CREATE_CALL) {
            for (int i = scroll; i < Math.min(scroll + 3, this.shownCreateCallPlayers.size()); i++) {
                var player = this.shownCreateCallPlayers.get(i);
                var toggle = this.toggleButtons[i - scroll];
                toggle.setActive(player.added);
                toggle.setLocked(player.locked);
                toggle.visible = true;
            }
        }
    }

    public void handleNetworkPacket(HologramNetwork network) {
        if (this.minecraft.player != null) {
            this.listedJoinCallPlayers.clear();

            var connection = this.minecraft.player.connection;
            var calls = network.getCalls(this.minecraft.player.getUUID());
//            calls.forEach(call -> this.listedJoinCallPlayers.add(CreateCallData.of(connection.getPlayerInfo(call.owner().uuid()), isPlayerMe(connection.getPlayerInfo(call.owner().uuid())))));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) { // TODO PLAYER LIST EMPTY SAY NO PLAYERS FOUND
        if (this.page == HologramScreen.Page.CREATE_CALL) {
            graphics.drawCenteredString(this.minecraft.font, Component.literal("Add Players to Call"), x + width / 2, y, -1);

            var y = this.y + 11;
            if (this.shownCreateCallPlayers.isEmpty()) graphics.drawCenteredString(this.minecraft.font, Component.literal("No Players Found").withStyle(ChatFormatting.BOLD, ChatFormatting.RED),
                    x + width / 2, y + 16, -1);
            for (int i = this.scroll; i < Math.min(this.scroll + 3, this.shownCreateCallPlayers.size()); i++) {
                var info = this.shownCreateCallPlayers.get(i).info;
                var me = this.isPlayerMe(info);
                var minY = y + this.height * (i - this.scroll);
                var name = me ? "You" : info.getProfile().getName();

                GraphicsUtil.fillRect(graphics, this.x, minY, this.width, this.height, this.color);
                PlayerFaceRenderer.draw(graphics, info.getSkinLocation(), this.x + 4, minY + 4, 24);
                graphics.drawString(this.minecraft.font, Component.literal(name).withStyle(ChatFormatting.BOLD).withStyle(me ? ChatFormatting.GOLD : ChatFormatting.WHITE), this.x + 34, minY + 12, -1);
            }
        } else {
            graphics.drawCenteredString(this.minecraft.font, Component.literal("Calls You can Join"), x + width / 2, y, -1);

            var y = this.y + 11;
            if (this.shownCreateCallPlayers.isEmpty()) graphics.drawCenteredString(this.minecraft.font, Component.literal("No Calls Found").withStyle(ChatFormatting.BOLD, ChatFormatting.RED),
                    x + width / 2, y + 16, -1);
        }
    }
    
    private boolean isPlayerMe(@Nullable PlayerInfo info) {
        return this.minecraft.player != null && info != null && info.getProfile().getId().equals(this.minecraft.player.getUUID());
    }

    public void setPage(HologramScreen.Page page) {
        this.page = page;
        this.lastSearch = "";

        this.refresh();
    }

    public void search(String search) {
        this.lastSearch = search;
        this.shownCreateCallPlayers.clear();
        if (search == null || search.isEmpty()) this.shownCreateCallPlayers.addAll(this.listedCreateCallPlayers);
        else this.shownCreateCallPlayers.addAll(this.listedCreateCallPlayers.stream().filter(s -> s.info.getProfile().getName().toLowerCase().contains(search.toLowerCase())).toList());
        this.updateMenu(0);
    }

    public void refresh() {
//        this.refreshPlayerList();
//        this.search(this.lastSearch);

        var network = new HologramNetwork();
        var tag = new CompoundTag();
        network.createCall(new HologramNetwork.Caller(this.minecraft.player.getUUID(), new WorldPos(this.minecraft.level.dimension(), this.minecraft.player.getOnPos()), false), List.of());
        network.save(tag);
        System.out.println(network.getCalls(this.minecraft.player.getUUID()));
        System.out.println(tag);
        network = HologramNetwork.load(tag);
        System.out.println(network.getCalls(this.minecraft.player.getUUID()));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.scroller.mouseScrolled(mouseX, mouseY, delta);
        return this.listedCreateCallPlayers.size() > 3;
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {}

    public List<HologramNetwork.Caller> getCallers() {
        return this.listedCreateCallPlayers.stream().filter(p -> p.added).map(p -> new HologramNetwork.Caller(p.info.getProfile().getId(), null, false)).toList();
    }

    public record JoinCallData(PlayerInfo info, UUID callId) {}

    public record CreateCallData(PlayerInfo info, boolean added, boolean locked) {
        public static CreateCallData of(PlayerInfo info, boolean isMe) {
            return new CreateCallData(info, isMe, isMe);
        }
    }
}
