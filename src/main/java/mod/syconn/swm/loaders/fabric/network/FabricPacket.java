package mod.syconn.swm.loaders.fabric.network;

import mod.syconn.swm.api.network.PacketDirection;
import mod.syconn.swm.api.network.message.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FabricPacket<T> {

    private final int index;
    private final Class<?> messageClass;
    private final BiConsumer<T, FriendlyByteBuf> encoder;
    private final Function<FriendlyByteBuf, T> decoder;
    private final BiConsumer<T, PacketContext> handler;
    private final PacketDirection direction;

    public FabricPacket(int index, Class<T> messageClass, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, PacketContext> handler, @Nullable PacketDirection direction) {
        this.index = index;
        this.messageClass = messageClass;
        this.direction = direction;
        this.encoder = encoder;
        this.decoder = decoder;
        this.handler = handler;
    }

    public int getIndex() {
        return this.index;
    }

    public Class<?> getMessageClass() {
        return this.messageClass;
    }

    @Nullable
    public PacketDirection getDirection() {
        return this.direction;
    }

    public void encode(T message, FriendlyByteBuf buf) {
        this.encoder.accept(message, buf);
    }

    public T decode(FriendlyByteBuf buf) {
        return this.decoder.apply(buf);
    }

    public void handle(T message, PacketContext context) {
        this.handler.accept(message, context);
    }
}
