package mod.syconn.swm.mixin;

import mod.syconn.swm.server.containers.SWGear;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Inventory.class)
public class InventoryMixin implements SWGear.SWGearAccess {

    @Unique
    private SWGear swm$SWGear;

    @Shadow
    @Final
    public Player player;

    @Override
    public SWGear swm$getSWGear() {
        return this.swm$SWGear;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Player player, CallbackInfo ci) {
        this.swm$SWGear = new SWGear(player);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void updateItems(CallbackInfo ci) {
        for (var i = 0; i < this.swm$SWGear.getContainerSize(); i++) this.swm$SWGear.getItem(i).inventoryTick(this.player.level(), this.player, i, false);
    }

    @Inject(method = "dropAll", at = @At("TAIL"))
    private void drop(CallbackInfo ci) {
        for (int i = 0; i < this.swm$getSWGear().getContainerSize(); i++) {
            ItemStack itemStack = this.swm$SWGear.getItem(i);
            if (!itemStack.isEmpty()) {
                this.player.drop(itemStack, true, false);
                this.swm$SWGear.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    @Inject(method = "replaceWith", at = @At("TAIL"))
    private void replace(Inventory playerInventory, CallbackInfo ci) {
        for (int i = 0; i < this.swm$SWGear.getContainerSize(); i++) swm$SWGear.setItem(i, playerInventory.getItem(i));
    }

    @Inject(method = "clearOrCountMatchingItems", at = @At("RETURN"), cancellable = true)
    private void commandClear(Predicate<ItemStack> stackPredicate, int maxCount, Container inventory, CallbackInfoReturnable<Integer> cir) {
        var i = cir.getReturnValueI();
        boolean bl = maxCount == 0;
        cir.setReturnValue(i += ContainerHelper.clearOrCountMatchingItems(this.swm$SWGear, stackPredicate, maxCount - i, bl));
    }
}
