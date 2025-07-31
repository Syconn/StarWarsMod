package mod.syconn.swm.mixin.client;

import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.serverside.SetEquipmentSlotPacket;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.server.containers.slot.EquipmentItemSlot;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow private static CreativeModeTab selectedTab;

    @Shadow private @Nullable Slot destroyItemSlot;

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "slotClicked", at = @At("TAIL"))
    private void clearInventory(Slot slot, int slotId, int mouseButton, ClickType type, CallbackInfo ci) {
        boolean bl = type == ClickType.QUICK_MOVE;
        if (slot == this.destroyItemSlot && bl && selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
            var gear = this.minecraft.player.swm$getSWGear();
            for (var i = 0; i < gear.getContainerSize(); i++) Network.CHANNEL.sendToServer(new SetEquipmentSlotPacket(this.minecraft.player.getUUID(), ItemStack.EMPTY, IEquipmentItem.SWEquipmentSlot.getSlot(i)));
        }
    }

    @Redirect(method = "selectTab", at = @At(value = "NEW", target = "(Lnet/minecraft/world/inventory/Slot;III)Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;"))
    private CreativeModeInventoryScreen.SlotWrapper redirectSlotWrapper(Slot originalSlot, int index, int x, int y) {
        if (originalSlot instanceof EquipmentItemSlot) return new CreativeModeInventoryScreen.SlotWrapper(originalSlot, originalSlot.index, 127, 20);
        return new CreativeModeInventoryScreen.SlotWrapper(originalSlot, index, x, y);
    }
}
