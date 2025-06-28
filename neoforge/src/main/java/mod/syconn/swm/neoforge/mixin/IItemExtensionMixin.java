package mod.syconn.swm.neoforge.mixin;

import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.util.client.IItemExtended;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {

    @Inject(method = "shouldCauseReequipAnimation", at = @At(value = "RETURN"), cancellable = true)
    default void allowAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged, CallbackInfoReturnable<Boolean> cir) {
        if (oldStack.getItem() instanceof IItemExtended && newStack.getItem() instanceof IItemExtended ext) {
            cir.setReturnValue(ClientHooks.shouldCauseReequipAnimation(ext, oldStack, newStack, slotChanged));
        }
    }
}
