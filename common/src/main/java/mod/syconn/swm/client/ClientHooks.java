package mod.syconn.swm.client;

import mod.syconn.swm.util.client.IItemExtended;
import net.minecraft.world.item.ItemStack;

public class ClientHooks {

    private static int slotMainHand = 0;

    public static boolean shouldCauseReequipAnimation(IItemExtended ext, ItemStack from, ItemStack to, int slot) {
        boolean fromInvalid = from.isEmpty();
        boolean toInvalid = to.isEmpty();

        if (fromInvalid && toInvalid) return false;
        if (fromInvalid || toInvalid) return true;

        boolean changed = false;
        if (slot != -1) {
            changed = slot != slotMainHand;
            slotMainHand = slot;
        }
        return shouldCauseReequipAnimation(ext, from, to, changed);
    }

    public static boolean shouldCauseReequipAnimation(IItemExtended ext, ItemStack from, ItemStack to, boolean changed) {
        return ext.allowUpdateAnimation(from, to, changed);
    }
}
