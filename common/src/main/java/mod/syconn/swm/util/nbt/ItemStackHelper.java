package mod.syconn.swm.util.nbt;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemStackHelper {

    public static void giveItem(Player player, ItemStack stack) {
        var bl = player.getInventory().add(stack);
        if (bl && stack.isEmpty()) {
            stack.setCount(1);
            var entity = player.drop(stack, false);
            if (entity != null) {
                entity.makeFakeItem();
            }

            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            var entity = player.drop(stack, false);
            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setTarget(player.getUUID());
            }
        }
    }
}
