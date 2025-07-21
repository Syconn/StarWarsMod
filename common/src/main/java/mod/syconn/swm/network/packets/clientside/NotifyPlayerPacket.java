package mod.syconn.swm.network.packets.clientside;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class NotifyPlayerPacket {

    private final Component msg;

    public NotifyPlayerPacket(Component msg) {
        this.msg = msg;
    }

    public NotifyPlayerPacket(FriendlyByteBuf buf) {
        this.msg = buf.readComponent();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeComponent(this.msg);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> context.get().getPlayer().sendSystemMessage(this.msg));
    }
}
