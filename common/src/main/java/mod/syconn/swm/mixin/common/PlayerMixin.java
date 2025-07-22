package mod.syconn.swm.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Unique
    private final Player player = (Player) (Object) this;

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 3)
    public boolean sweepingAttackLightsabers(boolean value) {
        return ((Player) (Object) this).getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem || value;
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;PLAYER_ATTACK_SWEEP:Lnet/minecraft/sounds/SoundEvent;"))
    public SoundEvent modifySweepAttack(SoundEvent original) {
        if (player.getMainHandItem().is(ModItems.LIGHTSABER.get())) return ModSounds.LIGHTSABER_SWING.get();
        return original;
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;PLAYER_ATTACK_CRIT:Lnet/minecraft/sounds/SoundEvent;"))
    public SoundEvent modifyCritAttack(SoundEvent original) {
        if (player.getMainHandItem().is(ModItems.LIGHTSABER.get())) return ModSounds.LIGHTSABER_SWING.get();
        return original;
    }
}
