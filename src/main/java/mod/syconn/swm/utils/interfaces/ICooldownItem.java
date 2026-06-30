package mod.syconn.swm.utils.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ICooldownItem {

    float getCooldownProgress(Player player, Level level, ItemStack stack, float tickDelta);
}
