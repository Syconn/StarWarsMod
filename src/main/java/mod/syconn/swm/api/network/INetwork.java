package mod.syconn.swm.api.network;

import mod.syconn.swm.api.network.message.IPacket;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.Supplier;

public interface INetwork {

    void sendToPlayer(Supplier<ServerPlayer> supplier, IPacket<?> message);

    void sendToAll(IPacket<?> message);

    void sendToTrackingLocation(Supplier<PacketLocation> supplier, IPacket<?> message);

    void sendToTrackingChunk(Supplier<LevelChunk> supplier, IPacket<?> message);

    void sendToNearbyPlayers(Supplier<PacketLocation> supplier, IPacket<?> message);

    void sendToServer(IPacket<?> message);

    boolean isActive(Connection connection);
}
