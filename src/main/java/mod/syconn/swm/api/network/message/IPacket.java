package mod.syconn.swm.api.network.message;

import net.minecraft.network.FriendlyByteBuf;

public interface IPacket<T> {
    void encode(T message, FriendlyByteBuf buffer);

    T decode(FriendlyByteBuf buffer);

    void handle(T message, PacketContext context);
}
