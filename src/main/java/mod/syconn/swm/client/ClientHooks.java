package mod.syconn.swm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swm.client.sounds.HoloProjectorSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {

    public static void overrideAbstractScreen(AbstractContainerScreen<?> screen, GuiGraphics graphics, int leftPos, int topPos) {
        if(screen instanceof InventoryScreen) {
            Player p = Minecraft.getInstance().player;
            if (p != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, leftPos + 76, topPos + 43, 7, 7, 18, 18, 256, 256);
            }
        } else if(screen instanceof CreativeModeInventoryScreen creative && creative.isInventoryOpen()) {
            Player p = Minecraft.getInstance().player;
            if (p != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, leftPos + 126, topPos + 19, 7, 7, 18, 18, 256, 256);
            }
        }
    }

    public static void playerHoloSound(BlockPos pos) {
        Minecraft.getInstance().getSoundManager().play(new HoloProjectorSoundInstance(pos));
    }
}
