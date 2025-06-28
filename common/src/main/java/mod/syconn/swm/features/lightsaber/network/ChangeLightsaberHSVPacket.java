package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ChangeLightsaberHSVPacket(BlockPos pos, int hsv) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ChangeLightsaberHSVPacket> TYPE = new CustomPacketPayload.Type<>(Constants.withId("change_lightsaber_hsv"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeLightsaberHSVPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ChangeLightsaberHSVPacket::pos,
            ByteBufCodecs.INT, ChangeLightsaberHSVPacket::hsv, ChangeLightsaberHSVPacket::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChangeLightsaberHSVPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer().level().getBlockEntity(packet.pos) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
                LightsaberTag.update(blockEntity.getContainer().getItem(0), t -> t.color = packet.hsv);
                blockEntity.markDirty();
            }
        });
    }
}
