package mod.syconn.swm.network.packets;

import dev.kosmx.playerAnim.core.util.Ease;
import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.utils.generic.AnimationUtil;
import mod.syconn.swm.utils.interfaces.IAnimatablePlayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class PlayAnimationPacket extends Packet<PlayAnimationPacket> {

    private final UUID uuid;
    private final String animation;
    private final int length;
    private final Ease ease;

    public PlayAnimationPacket(UUID uuid, String animation, int length, Ease ease) {
        this.uuid = uuid;
        this.animation = animation;
        this.length = length;
        this.ease = ease;
    }

    @Override
    public void encode(PlayAnimationPacket message, FriendlyByteBuf buffer) {
        buffer.writeUUID(message.uuid);
        buffer.writeUtf(message.animation);
        buffer.writeInt(message.length);
        buffer.writeEnum(message.ease);
    }

    @Override
    public PlayAnimationPacket decode(FriendlyByteBuf buffer) {
        return new PlayAnimationPacket(buffer.readUUID(), buffer.readUtf(), buffer.readInt(), buffer.readEnum(Ease.class));
    }

    @Override
    public void handle(PlayAnimationPacket message, PacketContext context) {
        context.execute(() -> {
            if (context.getPlayer() != null) AnimationUtil.notifyPlayers(context.getPlayer(), message.animation, message.length, message.ease);
            else if (context.getPlayer().level().getPlayerByUUID(message.uuid) instanceof AbstractClientPlayer player && player instanceof IAnimatablePlayer anim) anim.swm$playAnimation(message.animation, message.length, message.ease);
        });
        context.setHandled(true);
    }
}
