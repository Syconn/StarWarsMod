package mod.syconn.swm.utils.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IEquipmentItem {

    SWEquipmentSlot getSWEquipmentSlot();

    default void equipmentTick(ItemStack stack, Entity entity) {
        if (this instanceof Item item) item.inventoryTick(stack, entity.level(), entity, -1, false);
    }

    enum SWEquipmentSlot {
        LIGHTSABER,
        BLASTER_BACK,
        BLASTER_SIDE
    }
}
