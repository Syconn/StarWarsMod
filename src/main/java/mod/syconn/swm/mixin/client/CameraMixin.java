package mod.syconn.swm.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
@Environment(EnvType.CLIENT)
public abstract class CameraMixin {

    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void updateSetRotation(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        var minecraft = Minecraft.getInstance();

//        if (focusedEntity == minecraft.player) this.setRotation(this.xRot + BlasterRecoilManager.getHorizontalRecoilMovement(tickDelta), this.yRot + BlasterRecoilManager.getVerticalRecoilMovement(tickDelta)); TODO BLASTER RECOIL
    }
}
