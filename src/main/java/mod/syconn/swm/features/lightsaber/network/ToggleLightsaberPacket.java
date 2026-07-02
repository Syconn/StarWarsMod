package mod.syconn.swm.features.lightsaber.network;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.utils.client.SoundHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ToggleLightsaberPacket extends Packet<ToggleLightsaberPacket> {

    private final InteractionHand hand;
    private final boolean all;

    public ToggleLightsaberPacket(InteractionHand hand, boolean all) {
        this.hand = hand;
        this.all = all;
    }

    @Override
    public void encode(ToggleLightsaberPacket message, FriendlyByteBuf buffer) {
        buffer.writeEnum(message.hand);
        buffer.writeBoolean(message.all);
    }

    @Override
    public ToggleLightsaberPacket decode(FriendlyByteBuf buffer) {
        return new ToggleLightsaberPacket(buffer.readEnum(InteractionHand.class), buffer.readBoolean());
    }

    @Override
    public void handle(ToggleLightsaberPacket message, PacketContext context) {
        context.execute(() -> {
            Player player = context.getPlayer();

            if (player != null) {
                ItemStack stack = player.getItemInHand(message.hand);
                if (stack.getItem() instanceof LightsaberItem) LightsaberTag.update(stack, message.all ? LightsaberTag::toggleAll : LightsaberTag::togglePrimary);
                SoundHelper.playToggleAudio(player.level(), player.getOnPos().above(), LightsaberTag.getOrCreate(stack).isActive());
            }
        });
        context.setHandled(true);
    }
}