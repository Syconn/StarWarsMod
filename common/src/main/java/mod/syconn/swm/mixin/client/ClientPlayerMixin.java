package mod.syconn.swm.mixin.client;

import mod.syconn.swm.features.lightsaber.client.sound.IdleLightsaberSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
@Environment(EnvType.CLIENT)
public class ClientPlayerMixin {

    @Unique
    private boolean swm$metConditionsForLightsaberSound = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        var player = (Player)(Object)this;
        if (!player.level().isClientSide) return;

        var meetsConditionsForLightsaberSound = IdleLightsaberSoundInstance.areConditionsMet(player);
        if (meetsConditionsForLightsaberSound && !swm$metConditionsForLightsaberSound) {
            var minecraft = Minecraft.getInstance();
            minecraft.getSoundManager().play(new IdleLightsaberSoundInstance(player));
        }
        swm$metConditionsForLightsaberSound = meetsConditionsForLightsaberSound;
    }
}
