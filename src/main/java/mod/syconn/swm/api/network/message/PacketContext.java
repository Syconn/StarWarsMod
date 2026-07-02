package mod.syconn.swm.api.network.message;

import mod.syconn.swm.api.network.PacketDirection;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class PacketContext {

    private final PacketDirection direction;
    private IPacket<?> reply;
    private final ServerPlayer player;

    public PacketContext(PacketDirection direction, ServerPlayer player) {
        this.direction = direction;
        this.player = player;
    }

    @Nullable
    public PacketDirection getDirection() {
        return this.direction;
    }

    public void reply(IPacket<?> reply) {
        this.reply = reply;
    }

    @Nullable
    @SuppressWarnings("rawtypes")
    public IPacket getReply() {
        return this.reply;
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.player;
    }

    public abstract void setHandled(boolean handled);

    public abstract CompletableFuture<Void> execute(Runnable runnable);

    public abstract Connection getNetworkManager();
}
