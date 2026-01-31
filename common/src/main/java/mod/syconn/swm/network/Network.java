package mod.syconn.swm.network;

import dev.architectury.networking.NetworkChannel;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.features.lightsaber.network.*;
import mod.syconn.swm.network.packets.PlayAnimationPacket;
import mod.syconn.swm.network.packets.clientside.*;
import mod.syconn.swm.network.packets.serverside.HoloCallPacket;
import mod.syconn.swm.network.packets.serverside.RequestHologramPacket;
import mod.syconn.swm.network.packets.serverside.SetEquipmentSlotPacket;
import mod.syconn.swm.network.packets.ToggleEquipmentSlotPacket;
import mod.syconn.swm.utils.Constants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Objects;

public class Network {

    public static NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(ToggleLightsaberPacket.class, ToggleLightsaberPacket::encode, ToggleLightsaberPacket::new, ToggleLightsaberPacket::apply);
        CHANNEL.register(ThrowLightsaberPacket.class, ThrowLightsaberPacket::encode, ThrowLightsaberPacket::new, ThrowLightsaberPacket::apply);
        CHANNEL.register(SyncResourceDataPacket.class, SyncResourceDataPacket::encode, SyncResourceDataPacket::new, SyncResourceDataPacket::apply);
        CHANNEL.register(ChangeLightsaberHSVPacket.class, ChangeLightsaberHSVPacket::encode, ChangeLightsaberHSVPacket::new, ChangeLightsaberHSVPacket::apply);
        CHANNEL.register(CraftHiltPacket.class, CraftHiltPacket::encode, CraftHiltPacket::new, CraftHiltPacket::apply);
        CHANNEL.register(HoloCallPacket.class, HoloCallPacket::encode, HoloCallPacket::new, HoloCallPacket::apply);
        CHANNEL.register(RequestedHologramPacket.class, RequestedHologramPacket::encode, RequestedHologramPacket::new, RequestedHologramPacket::apply);
        CHANNEL.register(RequestHologramPacket.class, RequestHologramPacket::encode, RequestHologramPacket::new, RequestHologramPacket::apply);
        CHANNEL.register(MessagePlayerPacket.class, MessagePlayerPacket::encode, MessagePlayerPacket::new, MessagePlayerPacket::apply);
        CHANNEL.register(ToggleEquipmentSlotPacket.class, ToggleEquipmentSlotPacket::encode, ToggleEquipmentSlotPacket::new, ToggleEquipmentSlotPacket::apply);
        CHANNEL.register(PlayAnimationPacket.class, PlayAnimationPacket::encode, PlayAnimationPacket::new, PlayAnimationPacket::apply);
        CHANNEL.register(SetEquipmentSlotPacket.class, SetEquipmentSlotPacket::encode, SetEquipmentSlotPacket::new, SetEquipmentSlotPacket::apply);
        CHANNEL.register(PreciseEntityVelocityUpdatePacket.class, PreciseEntityVelocityUpdatePacket::write, PreciseEntityVelocityUpdatePacket::new, PreciseEntityVelocityUpdatePacket::apply);
        CHANNEL.register(ScorchBlockPacket.class, ScorchBlockPacket::encode, ScorchBlockPacket::new, ScorchBlockPacket::apply);
    }

    public static <T> void sendToNearby(ServerPlayer player, ResourceKey<Level> dimension, Vec3 pos, int radius, T message) {
        var playerlist = Objects.requireNonNull(GameInstance.getServer()).getPlayerList().getPlayers();
        for (ServerPlayer serverPlayer : playerlist) {
            if (serverPlayer != player && serverPlayer.level().dimension() == dimension) {
                double d = pos.x - serverPlayer.getX();
                double e = pos.y - serverPlayer.getY();
                double f = pos.z - serverPlayer.getZ();
                if (d * d + e * e + f * f < radius * radius) CHANNEL.sendToPlayer(serverPlayer, message);
            }
        }
    }

    public static Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
        Objects.requireNonNull(world, "The world cannot be null");
        Objects.requireNonNull(pos, "The chunk pos cannot be null");

        return world.getChunkSource().chunkMap.getPlayers(pos, false);
    }
}
