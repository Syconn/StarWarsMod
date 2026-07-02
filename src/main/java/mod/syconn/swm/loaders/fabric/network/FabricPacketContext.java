package mod.syconn.swm.loaders.fabric.network;

import mod.syconn.swm.api.network.PacketDirection;
import mod.syconn.swm.api.network.message.PacketContext;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricPacketContext extends PacketContext {
    private final Executor executor;
    private final Connection connection;

    public FabricPacketContext(Executor executor, Connection connection, @Nullable ServerPlayer player, PacketDirection direction) {
        super(direction, player);
        this.executor = executor;
        this.connection = connection;
    }

    @Override
    public void setHandled(boolean handled) {}

    @Override
    public CompletableFuture<Void> execute(Runnable runnable) {
        this.executor.execute(runnable);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Connection getNetworkManager() {
        return this.connection;
    }
}
