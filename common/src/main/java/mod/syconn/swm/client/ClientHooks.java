package mod.syconn.swm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.screen.HologramScreen;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.server.data.SWGear;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ClientHooks {

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static void overrideAbstractScreen(AbstractContainerScreen<?> screen, GuiGraphics graphics, int mouseX, int mouseY, float tickDelta) {
        if(screen instanceof InventoryScreen) {
            Player p = Minecraft.getInstance().player;
            if (p != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, screen.leftPos + 76, screen.topPos + 43, 7, 7, 18, 18, 256, 256);
            }
        } else if(screen instanceof CreativeModeInventoryScreen creative && creative.isInventoryOpen()) {
            Player p = Minecraft.getInstance().player;
            if (p != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, screen.leftPos + 126, screen.topPos + 19, 7, 7, 18, 18, 256, 256);
            }
        }
    }

    public static void renderHUD(GuiGraphics graphics, float tickDelta) {
        var gear = ((SWGear.SWGearAccess) Minecraft.getInstance().player).swm$getSWGear();
        if (!gear.getItemFromSlot(IEquipmentItem.SWEquipmentSlot.LIGHTSABER).isEmpty()) {
            var xOffset = Minecraft.getInstance().player.getMainArm() == HumanoidArm.RIGHT ? 91 : -127;
            graphics.blit(WIDGETS_LOCATION, graphics.guiWidth() / 2 + xOffset, graphics.guiHeight() - 23, 53, 22, 29, 24);
            renderSlot(graphics, graphics.guiWidth() / 2 + 11 + xOffset, graphics.guiHeight() - 19, tickDelta, Minecraft.getInstance().player, gear.getItemFromSlot(IEquipmentItem.SWEquipmentSlot.LIGHTSABER));
        }
    }

    public static Screen createHologramScreen(WorldPos worldPos, @Nullable ItemStack stack) {
        return new HologramScreen(worldPos, stack);
    }

    private static void renderSlot(GuiGraphics guiGraphics, int x, int y, float partialTick, Player player, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float)stack.getPopTime() - partialTick;
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float)(x + 8), (float)(y + 12), 0.0F);
                guiGraphics.pose().scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
            }

            guiGraphics.renderItem(player, stack, x, y, 1);
            if (f > 0.0F) guiGraphics.pose().popPose();
            guiGraphics.renderItemDecorations(GameInstance.getClient().font, stack, x, y);
        }
    }
}
