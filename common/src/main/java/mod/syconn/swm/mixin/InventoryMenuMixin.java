package mod.syconn.swm.mixin;

import mod.syconn.swm.server.containers.slot.EquipmentItemSlot;
import mod.syconn.swm.server.data.SWGear;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingContainer> {

    public InventoryMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void init(Inventory playerInventory, boolean active, Player owner, CallbackInfo ci) {
        this.addSlot(new EquipmentItemSlot(owner, IEquipmentItem.SWEquipmentSlot.LIGHTSABER, ((SWGear.SWGearAccess) owner).swm$getSWGear(), 0, 77, 44));
    }
}
