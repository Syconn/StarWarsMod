package mod.syconn.swm.mixin.client;

import mod.syconn.swm.utils.interfaces.ICustomHudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;)V"), cancellable = true)
    public void renderCrosshair(GuiGraphics guiGraphics, CallbackInfo ci) {
        assert this.minecraft.player != null;

        var mainHandStack = this.minecraft.player.getMainHandItem();
        var customHUDRenderer = ICustomHudRenderer.REGISTRY.get(mainHandStack.getItem().getClass());
        if (customHUDRenderer != null && customHUDRenderer.renderCrosshair(guiGraphics, this.minecraft.player, InteractionHand.MAIN_HAND, mainHandStack))
            ci.cancel();

        var offHandStack = this.minecraft.player.getOffhandItem();
        customHUDRenderer = ICustomHudRenderer.REGISTRY.get(offHandStack.getItem().getClass());
        if (customHUDRenderer != null && customHUDRenderer.renderCrosshair(guiGraphics, this.minecraft.player, InteractionHand.OFF_HAND, offHandStack))
            ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getDeltaFrameTime()F", shift = At.Shift.AFTER))
    public void render(GuiGraphics graphics, float tickDelta, CallbackInfo ci) {
        if (!this.minecraft.options.getCameraType().isFirstPerson()) return;

        assert this.minecraft.player != null;
        var mainHandStack = this.minecraft.player.getMainHandItem();
        var customHUDRenderer = ICustomHudRenderer.REGISTRY.get(mainHandStack.getItem().getClass());
        if (customHUDRenderer != null) customHUDRenderer.renderOverlay(graphics, this.minecraft.player, InteractionHand.MAIN_HAND, mainHandStack, screenWidth, screenHeight, tickDelta);
    }

//    @Inject(method = "getCameraPlayer", at = @At("HEAD"), cancellable = true)
//    void getCameraPlayer(CallbackInfoReturnable<Player> cir) {
//        var camEntity = this.minecraft.getCameraEntity();
//        if (camEntity instanceof MutableCameraEntity) cir.setReturnValue(this.minecraft.player);
//    }
}
