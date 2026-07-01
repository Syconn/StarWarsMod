package mod.syconn.swm.client.screen.hud;

import mod.syconn.swm.api.util.IHudOverlay;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SWGearHud implements IHudOverlay {

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    @Override
    public void draw(GuiGraphics graphics, float partialTick) {
        var player = Minecraft.getInstance().player;
        if (player instanceof SWGear.SWGearAccess access) {
            var gear = access.swm$getSWGear();
            if (!gear.getItemFromSlot(IEquipmentItem.SWEquipmentSlot.LIGHTSABER).isEmpty()) {
                var xOffset = player.getMainArm() == HumanoidArm.RIGHT ? 91 : -127;
                graphics.blit(WIDGETS_LOCATION, graphics.guiWidth() / 2 + xOffset, graphics.guiHeight() - 23, 53, 22, 29, 24);
                renderSlot(graphics, graphics.guiWidth() / 2 + 10 + xOffset, graphics.guiHeight() - 19, partialTick, player, gear.getItemFromSlot(IEquipmentItem.SWEquipmentSlot.LIGHTSABER));
            }
        }
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
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
        }
    }
}
