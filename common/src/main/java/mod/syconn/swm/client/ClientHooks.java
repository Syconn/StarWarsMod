package mod.syconn.swm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swm.client.screen.HologramScreen;
import mod.syconn.swm.utils.block.WorldPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ClientHooks {

    public static void overrideAbstractScreen(AbstractContainerScreen<?> screen, GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if(screen instanceof InventoryScreen) {
            Player p = Minecraft.getInstance().player;
            if (p != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // TODO SHOW IN CREATIVE
                graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, screen.leftPos + 76, screen.topPos + 43, 7, 7, 18, 18, 256, 256);
            }
        }
    }

    public static Screen createHologramScreen(WorldPos worldPos, @Nullable ItemStack stack) {
        return new HologramScreen(worldPos, stack);
    }
}
