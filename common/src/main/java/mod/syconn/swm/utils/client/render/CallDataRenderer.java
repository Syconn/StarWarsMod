package mod.syconn.swm.utils.client.render;

import com.google.common.collect.Lists;
import mod.syconn.swm.client.screen.components.ToggleButton;
import mod.syconn.swm.utils.client.GraphicsUtil;
import mod.syconn.swm.utils.math.ColorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CallDataRenderer implements Renderable, GuiEventListener, NarratableEntry {

    private final List<PlayerInfo> players = Lists.newArrayList();
    private final Minecraft minecraft = Minecraft.getInstance();
    private final int height = 32, width = 220;
    private final int x, y;
    private final int color = ColorUtil.packArgb(74, 74, 74, 255);
    private ToggleButton[] toggleButtons;

    public CallDataRenderer(int x, int y) {
        this.x = x;
        this.y = y;

        updatePlayerList();
        init();
    }

    private void updatePlayerList() {
        if (this.minecraft.player != null) {
            var connection = this.minecraft.player.connection;
            var uuids = connection.getOnlinePlayerIds();
            this.players.clear();
            uuids.forEach(uuid -> this.players.add(connection.getPlayerInfo(uuid))); // TODO REMOVE OWN PLAYER
//            uuids.forEach(uuid -> this.players.add(connection.getPlayerInfo(uuid)));
            this.toggleButtons = new ToggleButton[uuids.size()];
        }
    }

    private void init() {

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        for (int i = 0; i < this.players.size(); i++) {
            var info = this.players.get(i);
            var minY = y + this.height * i;

            GraphicsUtil.fillRect(graphics, x, minY, this.width, this.height, this.color);
            PlayerFaceRenderer.draw(graphics, info.getSkinLocation(), x + 4, minY + 4, 24);
            graphics.drawString(this.minecraft.font, Component.literal(info.getProfile().getName()).withStyle(ChatFormatting.BOLD), x + 34, y + 13, -1);
        }
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {}
}
