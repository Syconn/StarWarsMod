package mod.syconn.swm.server.containers.slot;

import com.mojang.datafixers.util.Pair;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.serverside.SetEquipmentSlotPacket;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EquipmentItemSlot extends Slot {

    private final Player player;
    private final IEquipmentItem.SWEquipmentSlot slot;

    public EquipmentItemSlot(Player player, IEquipmentItem.SWEquipmentSlot slot, Container container, int index, int x, int y) {
        super(container, index, x, y);
        System.out.println(player + " " + slot);

        this.player = player;
        this.slot = slot;
    }

    public void setChanged() {
        super.setChanged();
        player.getInventory().setChanged();
        if (player.level().isClientSide) Network.CHANNEL.sendToServer(new SetEquipmentSlotPacket(player.getUUID(), this.getItem(), this.slot));
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, Constants.withId("custom/lightsaber"));
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof IEquipmentItem && ((IEquipmentItem) stack.getItem()).getSWEquipmentSlot() == slot;
    }
}
