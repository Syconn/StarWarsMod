package mod.syconn.swm.utils.generic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtil {

    public static EquipmentSlot getEquipmentSlot(LivingEntity entity, ItemStack stack) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equipped = entity.getItemBySlot(slot);
            if (ItemStack.isSameItemSameTags(equipped, stack)) {
                return slot;
            }
        }
        return null;
    }

    public static int getSlotWithStack(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++)
            if (inventory.getItem(i) == stack) return i;
        return -1;
    }
}
