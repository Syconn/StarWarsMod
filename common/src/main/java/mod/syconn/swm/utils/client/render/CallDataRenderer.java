package mod.syconn.swm.utils.client.render;

import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.client.screen.components.ScrollWidget;
import mod.syconn.swm.client.screen.components.ToggleButton;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.client.GraphicsUtil;
import mod.syconn.swm.utils.client.WidgetComponent;
import mod.syconn.swm.utils.math.ColorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CallDataRenderer implements WidgetComponent {

    private final List<MenuData> players = new ArrayList<>();
    private final ToggleButton[] toggleButtons = new ToggleButton[3];
    private final Minecraft minecraft = Minecraft.getInstance();
    private final int color = ColorUtil.packArgb(74, 74, 74, 255);
    private final int height = 32, width = 220;
    private final int x, y;
    private int scroll = 0;
    private ScrollWidget scroller;

    public CallDataRenderer(int x, int y, Function<WidgetComponent, WidgetComponent> widgets) {
        this.x = x;
        this.y = y;

        refreshPlayerList();
        init(widgets);
        updateMenu(this.scroll);
    }

    private void init(Function<WidgetComponent, WidgetComponent> widgets) {
        widgets.apply(this);

        for (int i = 0; i < 3; i++) {
            final var v = i;
            this.toggleButtons[v] = (ToggleButton) widgets.apply(new ToggleButton(this.x + 180, this.y + 21 + this.height * v, false, ToggleButton.Color.GREEN, b -> this.toggled(b, v)));
        }
        this.scroller = (ScrollWidget) widgets.apply(new ScrollWidget(x + 207, y + 11, 91, this.players.size() - 3, w -> true, this::updateMenu));
    }

    private void toggled(ToggleButton button, int i) {
        var player = this.players.get(this.scroll + i);
        this.players.set(this.scroll + i, new MenuData(player.info, button.isActive(), player.locked));
    }

    private void refreshPlayerList() {
        if (this.minecraft.player != null) {
            this.players.clear();

            var connection = this.minecraft.player.connection;
            var uuids = connection.getOnlinePlayerIds();
            uuids.forEach(uuid -> this.players.add(MenuData.of(connection.getPlayerInfo(uuid), isPlayerMe(connection.getPlayerInfo(uuid)))));
            for (int i = 1; i < 3; i++) this.players.add(MenuData.of(ClientHooks.getInfo(ClientHooks.createMockPlayer(this.minecraft.level, "Test-Player" + i)), false));
        }
    }

    private void updateMenu(int scroll) {
        this.scroll = scroll;

        for (int i = scroll; i < Math.min(scroll + 3, this.players.size()); i++) {
            var player = this.players.get(i);
            var toggle = this.toggleButtons[i - scroll];
            toggle.setActive(player.added);
            toggle.setLocked(player.locked);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawCenteredString(this.minecraft.font, Component.literal("Add Players to Call"), x + width / 2, y, -1);

        var y = this.y + 11;
        for (int i = this.scroll; i < Math.min(this.scroll + 3, this.players.size()); i++) {
            var info = this.players.get(i).info;
            var minY = y + this.height * (i - this.scroll);
            var name = this.isPlayerMe(info) ? "You" : info.getProfile().getName();

            GraphicsUtil.fillRect(graphics, this.x, minY, this.width, this.height, this.color);
            PlayerFaceRenderer.draw(graphics, info.getSkinLocation(), this.x + 4, minY + 4, 24);
            graphics.drawString(this.minecraft.font, Component.literal(name).withStyle(ChatFormatting.BOLD), this.x + 34, minY + 12, -1);
        }
    }
    
    private boolean isPlayerMe(@Nullable PlayerInfo info) {
        return this.minecraft.player != null && info != null && info.getProfile().getId().equals(this.minecraft.player.getUUID());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.scroller.mouseScrolled(mouseX, mouseY, delta);
        return this.players.size() > 3;
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
        return this.players.stream().map(p -> new HologramNetwork.Caller(p.info.getProfile().getId(), Optional.empty(), false)).collect(Collectors.toList());
    }

    public record MenuData(PlayerInfo info, boolean added, boolean locked) {

        public static MenuData of(PlayerInfo info, boolean isMe) {
            return new MenuData(info, isMe, isMe);
        }
    }
}
