package mod.syconn.swm.api.registry.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

@FunctionalInterface
public interface ItemColorsRegister {
    void apply(ItemColor color, ItemLike... items);
}
