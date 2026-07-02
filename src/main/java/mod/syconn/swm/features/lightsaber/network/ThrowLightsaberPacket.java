package mod.syconn.swm.features.lightsaber.network;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaberEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public class ThrowLightsaberPacket extends Packet<ThrowLightsaberPacket> {

    private final InteractionHand hand;

    public ThrowLightsaberPacket(InteractionHand hand) {
        this.hand = hand;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand) ;
    }

    @Override
    public void encode(ThrowLightsaberPacket message, FriendlyByteBuf buffer) {
        buffer.writeEnum(message.hand);
    }

    @Override
    public ThrowLightsaberPacket decode(FriendlyByteBuf buffer) {
        return new ThrowLightsaberPacket(buffer.readEnum(InteractionHand.class));
    }

    @Override
    public void handle(ThrowLightsaberPacket message, PacketContext context) {
        context.execute(() -> {
            var player = context.getPlayer();

            if (player != null) {
                ThrownLightsaberEntity thrownLightsaber = new ThrownLightsaberEntity(player.level(), player, message.hand);
                thrownLightsaber.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                if (!player.isCreative()) player.getItemInHand(hand).shrink(1);
                player.level().addFreshEntity(thrownLightsaber);

                if (!player.getAbilities().instabuild) player.getItemInHand(message.hand).shrink(1);
            }
        });
        context.setHandled(true);
    }
}
