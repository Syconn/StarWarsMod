package mod.syconn.swm.mixin;

import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyVariable(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("STORE"), ordinal = 3)
    public boolean sweepingAttackLightsabers(boolean value) {
        return ((Player) (Object) this).getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem || value;
    }
}
