package mod.syconn.swm.utils.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IEquipmentItem {

    SWEquipmentSlot getSWEquipmentSlot();

    enum SWEquipmentSlot {
        LIGHTSABER,
        BLASTER_BACK,
        BLASTER_SIDE
    }
}
