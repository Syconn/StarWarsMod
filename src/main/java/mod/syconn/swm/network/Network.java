package mod.syconn.swm.network;

import mod.syconn.swm.api.network.INetwork;
import mod.syconn.swm.api.network.NetworkManager;
import mod.syconn.swm.api.network.PacketDirection;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.network.CraftHiltPacket;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.packets.*;

public class Network {

    public static INetwork CHANNEL;

    public static void init() {
        CHANNEL = NetworkManager.createNetwork("network")
                .registerPlayMessage(PreciseEntityVelocityUpdatePacket.class, PacketDirection.PLAY_CLIENT_BOUND)
                .registerPlayMessage(PlayAnimationPacket.class)
                .registerPlayMessage(ToggleLightsaberPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(ThrowLightsaberPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(SyncResourceDataPacket.class, PacketDirection.PLAY_CLIENT_BOUND)
                .registerPlayMessage(ChangeLightsaberHSVPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(CraftHiltPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(ToggleLightsaberPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(ToggleEquipmentSlotPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(SetEquipmentSlotPacket.class, PacketDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(ScorchBlockPacket.class, PacketDirection.PLAY_CLIENT_BOUND)
                .build();
    }

//    public static <T> void sendToNearby(ServerPlayer player, ResourceKey<Level> dimension, Vec3 pos, int radius, T message) { TODO REMOVE OR RE ADD
//        var playerlist = Objects.requireNonNull(GameInstance.getServer()).getPlayerList().getPlayers();
//        for (ServerPlayer serverPlayer : playerlist) {
//            if (serverPlayer != player && serverPlayer.level().dimension() == dimension) {
//                double d = pos.x - serverPlayer.getX();
//                double e = pos.y - serverPlayer.getY();
//                double f = pos.z - serverPlayer.getZ();
//                if (d * d + e * e + f * f < radius * radius) CHANNEL.sendToPlayer(serverPlayer, message);
//            }
//        }
//    }
//
//    public static Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
//        Objects.requireNonNull(world, "The world cannot be null");
//        Objects.requireNonNull(pos, "The chunk pos cannot be null");
//
//        return world.getChunkSource().chunkMap.getPlayers(pos, false);
//    }
}
