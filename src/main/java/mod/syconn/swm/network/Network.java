package mod.syconn.swm.network;

import mod.syconn.swm.api.network.INetwork;
import mod.syconn.swm.api.network.NetworkManager;
import mod.syconn.swm.api.network.PacketDirection;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.network.CraftHiltPacket;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.packets.*;
import mod.syconn.swm.network.packets.MessagePlayerPacket;
import mod.syconn.swm.network.packets.serverside.HoloCallPacket;
import mod.syconn.swm.network.packets.serverside.RequestHologramPacket;
import mod.syconn.swm.network.packets.SetEquipmentSlotPacket;

public class Network {

    public static INetwork CHANNEL;

    public static void init() {
        CHANNEL = NetworkManager.createNetwork("network")
                .registerPlayMessage(MessagePlayerPacket.class, PacketDirection.PLAY_CLIENT_BOUND)
                .registerPlayMessage(PlayAnimationPacket.class)
                .registerPlayMessage()
                .build();



        CHANNEL.register(ToggleLightsaberPacket.class, ToggleLightsaberPacket::encode, ToggleLightsaberPacket::new, ToggleLightsaberPacket::apply);
        CHANNEL.register(ThrowLightsaberPacket.class, ThrowLightsaberPacket::encode, ThrowLightsaberPacket::new, ThrowLightsaberPacket::apply);
        CHANNEL.register(SyncResourceDataPacket.class, SyncResourceDataPacket::encode, SyncResourceDataPacket::new, SyncResourceDataPacket::apply);
        CHANNEL.register(ChangeLightsaberHSVPacket.class, ChangeLightsaberHSVPacket::encode, ChangeLightsaberHSVPacket::new, ChangeLightsaberHSVPacket::apply);
        CHANNEL.register(CraftHiltPacket.class, CraftHiltPacket::encode, CraftHiltPacket::new, CraftHiltPacket::apply);
        CHANNEL.register(ToggleEquipmentSlotPacket.class, ToggleEquipmentSlotPacket::encode, ToggleEquipmentSlotPacket::new, ToggleEquipmentSlotPacket::apply);
        CHANNEL.register(SetEquipmentSlotPacket.class, SetEquipmentSlotPacket::encode, SetEquipmentSlotPacket::new, SetEquipmentSlotPacket::apply);
        CHANNEL.register(PreciseEntityVelocityUpdatePacket.class, PreciseEntityVelocityUpdatePacket::write, PreciseEntityVelocityUpdatePacket::new, PreciseEntityVelocityUpdatePacket::apply);
        CHANNEL.register(ScorchBlockPacket.class, ScorchBlockPacket::encode, ScorchBlockPacket::new, ScorchBlockPacket::apply);
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
