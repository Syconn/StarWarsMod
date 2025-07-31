package mod.syconn.swm.network.packets.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.utils.generic.ListUtil;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Supplier;

public class SetEquipmentSlotPacket {

    private final UUID target;
    private final ItemStack stack;
    private final IEquipmentItem.SWEquipmentSlot slot;

    public SetEquipmentSlotPacket(UUID target, ItemStack stack, IEquipmentItem.SWEquipmentSlot slot) {
        this.target = target;
        this.stack = stack;
        this.slot = slot;
    }

    public SetEquipmentSlotPacket(FriendlyByteBuf buf) {
        this.target = buf.readUUID();
        this.stack = buf.readItem();
        this.slot = buf.readEnum(IEquipmentItem.SWEquipmentSlot.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.target);
        buf.writeItem(this.stack);
        buf.writeEnum(this.slot);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() != null) {
                var player = context.get().getPlayer().level().getPlayerByUUID(this.target);
                if (player != null) player.getInventory().swm$getSWGear().setItem(this.slot, this.stack);
            }
        });
    }
}
