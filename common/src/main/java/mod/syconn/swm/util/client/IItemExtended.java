package mod.syconn.swm.util.client;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IItemExtended {

    default boolean allowUpdateAnimation(@NotNull ItemStack from, @NotNull ItemStack to, boolean changed) {
        return true;
    }
}
