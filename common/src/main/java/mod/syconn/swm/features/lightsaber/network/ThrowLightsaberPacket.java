package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaber;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.codec.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public record ThrowLightsaberPacket(InteractionHand hand) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ThrowLightsaberPacket> TYPE = new CustomPacketPayload.Type<>(Constants.withId("throw_lightsaber"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ThrowLightsaberPacket> STREAM_CODEC = StreamCodec.composite(StreamCodecs.enumCodec(InteractionHand.class), ThrowLightsaberPacket::hand, ThrowLightsaberPacket::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ThrowLightsaberPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();

            if (player != null) {
                ThrownLightsaber thrownLightsaber = new ThrownLightsaber(player.level(), player, packet.hand);
                thrownLightsaber.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                if (!player.isCreative()) player.getItemInHand(packet.hand).shrink(1);
                player.level().addFreshEntity(thrownLightsaber);
            }
        });
    }
}
