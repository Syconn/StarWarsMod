package mod.syconn.swm.network.packets;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SetEquipmentSlotPacket extends Packet<SetEquipmentSlotPacket> {

    private final UUID target;
    private final ItemStack stack;
    private final IEquipmentItem.SWEquipmentSlot slot;

    public SetEquipmentSlotPacket(UUID target, ItemStack stack, IEquipmentItem.SWEquipmentSlot slot) {
        this.target = target;
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public void encode(SetEquipmentSlotPacket message, FriendlyByteBuf buffer) {
        buffer.writeUUID(message.target);
        buffer.writeItem(message.stack);
        buffer.writeEnum(message.slot);
    }

    @Override
    public SetEquipmentSlotPacket decode(FriendlyByteBuf buffer) {
        return new SetEquipmentSlotPacket(buffer.readUUID(), buffer.readItem(), buffer.readEnum(IEquipmentItem.SWEquipmentSlot.class));
    }

    @Override
    public void handle(SetEquipmentSlotPacket message, PacketContext context) {
        context.execute(() -> {
            if (context.getPlayer() != null) {
                var player = context.getPlayer().level().getPlayerByUUID(this.target);
                if (player instanceof SWGear.SWGearAccess access) access.swm$getSWGear().setItem(this.slot, this.stack);
            }
        });
        context.setHandled(true);
    }
}
