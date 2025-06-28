package mod.syconn.swm.fabric.mixin;

import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.util.client.IItemExtended;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FabricItem.class)
public interface FabricItemMixin {

    @Inject(method = "allowComponentsUpdateAnimation", at = @At("RETURN"), cancellable = true)
    default void allowAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack, CallbackInfoReturnable<Boolean> cir) {
        if (oldStack.getItem() instanceof IItemExtended && newStack.getItem() instanceof IItemExtended ext) {
            cir.setReturnValue(ClientHooks.shouldCauseReequipAnimation(ext, oldStack, newStack, hand == InteractionHand.OFF_HAND ? -1 : player.getInventory().selected));
        }
    }
}
