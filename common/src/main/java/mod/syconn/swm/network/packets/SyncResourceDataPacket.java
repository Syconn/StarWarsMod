package mod.syconn.swm.network.packets;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.server.SyncedResourceManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncResourceDataPacket(ResourceLocation id, CompoundTag data) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncResourceDataPacket> TYPE = new CustomPacketPayload.Type<>(Constants.withId("sync_resource_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncResourceDataPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SyncResourceDataPacket::id, ByteBufCodecs.COMPOUND_TAG, SyncResourceDataPacket::data, SyncResourceDataPacket::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncResourceDataPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer() instanceof LocalPlayer player) {
                var data = SyncedResourceManager.getLoginDataSupplier(packet.id);
                var synced = data.readData(packet.data);
                if (!synced) player.connection.getConnection().disconnect(Component.literal("Connection closed - [" + Constants.MOD + "] failed to load " + packet.id.getPath()));
            }
        });
    }
}
