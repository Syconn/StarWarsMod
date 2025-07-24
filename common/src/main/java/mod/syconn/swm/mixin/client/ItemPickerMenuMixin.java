package mod.syconn.swm.mixin.client;

import mod.syconn.swm.server.containers.slot.EquipmentItemSlot;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class ItemPickerMenuMixin {

    @Redirect(method = "selectTab", at = @At(value = "NEW", target = "(Lnet/minecraft/world/inventory/Slot;III)Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;"))
    private CreativeModeInventoryScreen.SlotWrapper redirectSlotWrapper(Slot originalSlot, int index, int x, int y) {
        if (originalSlot instanceof EquipmentItemSlot) return new CreativeModeInventoryScreen.SlotWrapper(originalSlot, originalSlot.index, 127, 20);
        return new CreativeModeInventoryScreen.SlotWrapper(originalSlot, index, x, y);
    }
}
