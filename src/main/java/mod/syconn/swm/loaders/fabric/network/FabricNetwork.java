package mod.syconn.swm.loaders.fabric.network;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import mod.syconn.swm.api.network.INetwork;
import mod.syconn.swm.api.network.PacketLocation;
import mod.syconn.swm.api.network.message.IPacket;
import mod.syconn.swm.api.util.Env;
import mod.syconn.swm.api.util.Environment;
import mod.syconn.swm.utils.Constants;
import net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.SectionPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FabricNetwork implements INetwork {

    private final ResourceLocation id;
    private final Map<Class<?>, FabricPacket<?>> classToPlayMessage;
    private final Map<Integer, FabricPacket<?>> indexToPlayMessage;
    private MinecraftServer server;
    private boolean active = false;

    public FabricNetwork(String id, List<FabricPacket<?>> playMessages) {
        this.id = Constants.withId(id);
        this.classToPlayMessage = createClassMap(playMessages);
        this.indexToPlayMessage = createIndexMap(playMessages);
        this.setup();
    }

    private void setup() {
        if(!this.classToPlayMessage.isEmpty()) {
            // Only register client receiver only if on physical client
            Environment.runOn(Env.CLIENT, () -> () -> {
                ClientPlayNetworking.registerGlobalReceiver(this.id, (client, handler, buf, responseSender) -> {
                    receivePlay(this, client, handler, buf, responseSender);
                });
            });
            ServerPlayNetworking.registerGlobalReceiver(this.id, (server, player, handler, buf, responseSender) -> {
                receivePlay(this, server, player, handler, buf, responseSender);
            });
        }

        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.server = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> this.server = null);

        Environment.runOn(Env.CLIENT, () -> () -> {
            C2SPlayChannelEvents.REGISTER.register((handler, sender, client, channels) -> {
                this.active = channels.contains(this.id);
            });
            C2SPlayChannelEvents.UNREGISTER.register((handler, sender, client, channels) -> {
                if(channels.contains(this.id)) {
                    this.active = false;
                }
            });
        });
        Environment.runOn(Env.SERVER, () -> () -> {
            S2CPlayChannelEvents.REGISTER.register((handler, sender, server, channels) -> {
                this.active = channels.contains(this.id);
            });
            S2CPlayChannelEvents.UNREGISTER.register((handler, sender, server, channels) -> {
                if(channels.contains(this.id)) {
                    this.active = false;
                }
            });
        });
    }

    @Override
    public void sendToPlayer(Supplier<ServerPlayer> supplier, IPacket<?> message) {
        FriendlyByteBuf buf = this.encode(message);
        ServerPlayNetworking.send(supplier.get(), this.id, buf);
    }

    @Override
    public void sendToTrackingLocation(Supplier<PacketLocation> supplier, IPacket<?> message) {
        this.sendToTrackingChunk(() -> {
            PacketLocation location = supplier.get();
            Vec3 pos = location.pos();
            int chunkX = SectionPos.blockToSectionCoord(pos.x);
            int chunkZ = SectionPos.blockToSectionCoord(pos.z);
            return location.level().getChunk(chunkX, chunkZ);
        }, message);
    }

    @Override
    public void sendToTrackingChunk(Supplier<LevelChunk> supplier, IPacket<?> message) {
        LevelChunk chunk = supplier.get();
        FriendlyByteBuf buf = this.encode(message);
        Packet<ClientGamePacketListener> packet = ServerPlayNetworking.createS2CPacket(this.id, buf);
        ((ServerChunkCache) chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).forEach(e -> e.connection.send(packet));
    }

    @Override
    public void sendToNearbyPlayers(Supplier<PacketLocation> supplier, IPacket<?> message) {
        PacketLocation location = supplier.get();
        Level level = location.level();
        Vec3 pos = location.pos();
        FriendlyByteBuf buf = this.encode(message);
        Packet<ClientGamePacketListener> packet = ServerPlayNetworking.createS2CPacket(this.id, buf);
        this.server.getPlayerList().broadcast(null, pos.x, pos.y, pos.z, location.range(), level.dimension(), packet);
    }

    @Override
    public void sendToServer(IPacket<?> message) {
        FriendlyByteBuf buf = this.encode(message);
        ClientPlayNetworking.send(this.id, buf);
    }

    @Override
    public void sendToAll(IPacket<?> message) {
        FriendlyByteBuf buf = this.encode(message);
        Packet<ClientGamePacketListener> packet = ServerPlayNetworking.createS2CPacket(this.id, buf);
        this.server.getPlayerList().broadcastAll(packet);
    }

    @Override
    public boolean isActive(Connection connection) {
        return connection.isConnected() && this.active;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private FriendlyByteBuf encode(IPacket<?> message) {
        FabricPacket fabricMessage = this.classToPlayMessage.get(message.getClass());
        Preconditions.checkNotNull(fabricMessage);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(fabricMessage.getIndex());
        fabricMessage.encode(message, buf);
        return buf;
    }

    private static <T extends FabricPacket<?>> Map<Class<?>, T> createClassMap(Collection<T> c) {
        Object2ObjectMap<Class<?>, T> map = new Object2ObjectArrayMap<>();
        c.forEach(msg -> map.put(msg.getMessageClass(), msg));
        return Collections.unmodifiableMap(map);
    }

    private static <T extends FabricPacket<?>> Map<Integer, T> createIndexMap(Collection<T> c) {
        Int2ObjectMap<T> map = new Int2ObjectArrayMap<>();
        c.forEach(msg -> map.put(msg.getIndex(), msg));
        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void receivePlay(FabricNetwork network, Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buf, PacketSender packetSender) {
        int index = buf.readInt();
        FabricPacket message = network.indexToPlayMessage.get(index);
        if(validateMessage(message, listener.getConnection())) return;
        IPacket<?> msg = (IPacket<?>) message.decode(buf);
        message.handle(msg, new FabricPacketContext(minecraft, listener.getConnection(), null, message.getDirection()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void receivePlay(FabricNetwork network, MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
        int index = buf.readInt();
        FabricPacket message = network.indexToPlayMessage.get(index);
        if(validateMessage(message, handler.connection)) return;
        IPacket<?> msg = (IPacket<?>) message.decode(buf);
        message.handle(msg, new FabricPacketContext(server, handler.connection, player, message.getDirection()));
    }

    private boolean validateMessage(@Nullable FabricPacket<?> message, Connection connection) {
        if(message == null) {
            connection.disconnect(Component.literal("Received invalid packet, closing connection"));
            return true;
        }
        var direction = message.getDirection();
        if (direction != null && !direction.isClient()) {
            connection.disconnect(Component.literal("Received invalid packet, closing connection"));
            return true;
        }
        return false;
    }
}
