package mod.syconn.swm.network.packets;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.server.SyncedResourceManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SyncResourceDataPacket {

    private final ResourceLocation id;
    private final FriendlyByteBuf data;

    public SyncResourceDataPacket(ResourceLocation id, FriendlyByteBuf data) {
        this.id = id;
        this.data = data;
    }

    public SyncResourceDataPacket(FriendlyByteBuf buf) {
        this.id = buf.readResourceLocation();
        var readableBytes = buf.readVarInt();
        this.data = new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readBytes(readableBytes)));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.id);
        buf.writeVarInt(this.data.readableBytes());
        buf.writeBytes(this.data);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() instanceof LocalPlayer player) {
                SyncedResourceManager.ISyncedData data = SyncedResourceManager.getLoginDataSupplier(this.id);
                boolean synced = data.readData(this.data);
                if (!synced) player.connection.getConnection().disconnect(Component.literal("Connection closed - [" + Constants.MOD + "] failed to load " + this.id.getPath()));
            }
        });
    }
}
