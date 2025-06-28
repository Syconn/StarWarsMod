package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.codec.StreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ToggleLightsaberPacket(InteractionHand hand) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleLightsaberPacket> TYPE = new CustomPacketPayload.Type<>(Constants.withId("toggle_lightsaber"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleLightsaberPacket> STREAM_CODEC = StreamCodec.composite(StreamCodecs.enumCodec(InteractionHand.class), ToggleLightsaberPacket::hand, ToggleLightsaberPacket::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ToggleLightsaberPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();

            if (player != null) {
                ItemStack stack = player.getItemInHand(packet.hand);
                if (stack.getItem() instanceof LightsaberItem) LightsaberComponent.update(stack, LightsaberComponent::toggle);
            }
        });
    }
}