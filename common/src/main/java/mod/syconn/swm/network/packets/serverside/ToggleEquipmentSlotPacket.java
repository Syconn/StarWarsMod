package mod.syconn.swm.network.packets.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.server.data.SWGear;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class ToggleEquipmentSlotPacket {

    private final int openSlot;
    private final int selectedSlot;
    private final IEquipmentItem.SWEquipmentSlot equipmentSlot;

    public ToggleEquipmentSlotPacket(int openSlot, int selectedSlot, IEquipmentItem.SWEquipmentSlot equipmentSlot) {
        this.openSlot = openSlot;
        this.selectedSlot = selectedSlot;
        this.equipmentSlot = equipmentSlot;
    }

    public ToggleEquipmentSlotPacket(FriendlyByteBuf buf) {
        this.openSlot = buf.readInt();
        this.selectedSlot = buf.readInt();
        this.equipmentSlot = buf.readEnum(IEquipmentItem.SWEquipmentSlot.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.openSlot);
        buf.writeInt(this.selectedSlot);
        buf.writeEnum(this.equipmentSlot);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            var player = context.get().getPlayer();
            if (player != null) {
                final var gear = ((SWGear.SWGearAccess) player).swm$getSWGear();
                if (gear.getItemFromSlot(this.equipmentSlot).isEmpty() && player.getInventory().getItem(this.selectedSlot).getItem() instanceof IEquipmentItem item && item.getSWEquipmentSlot().equals(this.equipmentSlot))
                    gear.setItem(this.equipmentSlot, player.getInventory().removeItemNoUpdate(this.selectedSlot));
                else if (!gear.getItemFromSlot(this.equipmentSlot).isEmpty() && this.openSlot != -1) {
                    player.getInventory().setItem(this.openSlot, gear.removeItemNoUpdate(this.equipmentSlot));
                    player.getInventory().selected = this.openSlot;
                }
                player.inventoryMenu.broadcastChanges();
            }
        });
    }
}
