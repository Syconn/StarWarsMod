package mod.syconn.swm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.server.data.SWGear;
import mod.syconn.swm.utils.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements SWGear.SWGearAccess {

    @Unique
    private final Player swm$player = (Player) (Object) this;

    @Unique
    public SWGear swm$SWGear = null;

    @Override
    public SWGear swm$getSWGear() {
        if (this.swm$SWGear == null) this.swm$SWGear = new SWGear(this.swm$player);
        return this.swm$SWGear;
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (this.swm$SWGear != null) compound.put(Constants.MOD + ":swGear", this.swm$SWGear.save());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compound, CallbackInfo info) {
        if (compound.contains(Constants.MOD + ":swGear")) this.swm$getSWGear().load(compound.getCompound(Constants.MOD + ":swGear"));
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 3)
    public boolean sweepingAttackLightsabers(boolean value) {
        return ((Player) (Object) this).getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem || value;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 4)
    public SoundEvent modifySweepAttack(SoundEvent sound) {
        if (this.swm$player.getMainHandItem().is(ModItems.LIGHTSABER.get())) return ModSounds.LIGHTSABER_SWING.get();
        return sound;
    }
}
