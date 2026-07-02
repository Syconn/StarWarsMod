package mod.syconn.swm.network.packets;

import dev.kosmx.playerAnim.core.util.Ease;
import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.utils.generic.AnimationUtil;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.network.FriendlyByteBuf;

public class ToggleEquipmentSlotPacket extends Packet<ToggleEquipmentSlotPacket> {

    private final int openSlot;
    private final int selectedSlot;
    private final IEquipmentItem.SWEquipmentSlot equipmentSlot;

    public ToggleEquipmentSlotPacket(int openSlot, int selectedSlot, IEquipmentItem.SWEquipmentSlot equipmentSlot) {
        this.openSlot = openSlot;
        this.selectedSlot = selectedSlot;
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public void encode(ToggleEquipmentSlotPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.openSlot);
        buffer.writeInt(message.selectedSlot);
        buffer.writeEnum(message.equipmentSlot);
    }

    @Override
    public ToggleEquipmentSlotPacket decode(FriendlyByteBuf buffer) {
        return new ToggleEquipmentSlotPacket(buffer.readInt(), buffer.readInt(), buffer.readEnum(IEquipmentItem.SWEquipmentSlot.class));
    }

    @Override
    public void handle(ToggleEquipmentSlotPacket message, PacketContext context) {
        context.execute(() -> {
            var player = context.getPlayer();
            if (player instanceof SWGear.SWGearAccess access) {
                final var gear = access.swm$getSWGear();
                if (gear.getItemFromSlot(message.equipmentSlot).isEmpty() && player.getInventory().getItem(message.selectedSlot).getItem() instanceof IEquipmentItem item
                        && item.getSWEquipmentSlot().equals(message.equipmentSlot)) {
                    gear.setItem(message.equipmentSlot, player.getInventory().removeItemNoUpdate(message.selectedSlot));
                    AnimationUtil.notifyPlayers(player, "return.swap.lightsaber", 1, Ease.INCUBIC);
                } else if (!gear.getItemFromSlot(message.equipmentSlot).isEmpty() && player.getInventory().getItem(message.selectedSlot).getItem() instanceof IEquipmentItem item
                        && item.getSWEquipmentSlot().equals(message.equipmentSlot)) {
                    var gearItem = gear.getItemFromSlot(message.equipmentSlot).copy();
                    gear.setItem(message.equipmentSlot, player.getInventory().removeItemNoUpdate(message.selectedSlot));
                    player.getInventory().setItem(message.selectedSlot, gearItem);
                    AnimationUtil.notifyPlayers(player, "grab.swap.lightsaber", 1, Ease.INCUBIC);
                } else if (!gear.getItemFromSlot(message.equipmentSlot).isEmpty() && message.openSlot != -1) {
                    player.getInventory().setItem(message.openSlot, gear.removeItemNoUpdate(message.equipmentSlot));
                    player.getInventory().selected = message.openSlot;
                    AnimationUtil.notifyPlayers(player, "grab.swap.lightsaber", 1, Ease.INCUBIC);
                }
            }
        });
        context.setHandled(true);
    }
}
