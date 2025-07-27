package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.sound.LightsaberAudio;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ToggleLightsaberPacket {

    private final InteractionHand hand;
    private final boolean all;

    public ToggleLightsaberPacket(InteractionHand hand, boolean all) {
        this.hand = hand;
        this.all = all;
    }

    public ToggleLightsaberPacket(FriendlyByteBuf buf) {
        this(buf.readEnum(InteractionHand.class), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
        buf.writeBoolean(this.all);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();

            if (player != null) { // TODO HANDLE MULTI BLADE SUPPORT
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof LightsaberItem) LightsaberTag.update(stack, this.all ? LightsaberTag::toggleAll : LightsaberTag::togglePrimary);
                LightsaberAudio.playToggleAudio(player.level(), player.getOnPos().above(), LightsaberTag.getOrCreate(stack).isActive());
            }
        });
    }
}