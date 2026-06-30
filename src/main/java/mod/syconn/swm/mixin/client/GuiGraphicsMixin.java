package mod.syconn.swm.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.utils.interfaces.ICooldownItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void renderGuiItemOverlay(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        var mc = Minecraft.getInstance();
        if (!stack.isEmpty() && stack.getItem() instanceof ICooldownItem && mc.screen == null) {
            var clientPlayerEntity = mc.player;
            var f = clientPlayerEntity == null ? 0.0F : ((ICooldownItem)stack.getItem()).getCooldownProgress(clientPlayerEntity, clientPlayerEntity.level(), stack, StarWarsClient.getTickDelta());
            if (f > 0.0F) {
                RenderSystem.disableDepthTest();
                ((GuiGraphics)(Object)this).fill(x, y + Mth.floor(16.0F * (1.0F - f)), x + 16, y + 16, 0x7fffffff);
                RenderSystem.enableDepthTest();
            }
        }
    }
}
