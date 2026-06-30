package mod.syconn.swm.network.packets;

import dev.architectury.networking.NetworkManager;
import dev.kosmx.playerAnim.core.util.Ease;
import mod.syconn.swm.utils.generic.AnimationUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayAnimationPacket {

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

    public PlayAnimationPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
        this.animation = buf.readUtf();
        this.length = buf.readInt();
        this.ease = buf.readEnum(Ease.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeUtf(this.animation);
        buf.writeInt(this.length);
        buf.writeEnum(this.ease);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() instanceof ServerPlayer player) AnimationUtil.notifyPlayers(player, this.animation, this.length, this.ease);
            else if (context.get().getPlayer().level().getPlayerByUUID(this.uuid) instanceof AbstractClientPlayer player) player.swm$playAnimation(this.animation, this.length, this.ease);
        });
    }
}
