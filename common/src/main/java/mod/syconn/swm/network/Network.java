package mod.syconn.swm.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.network.packets.SyncResourceDataPacket;
import mod.syconn.swm.network.packets.ThrowLightsaberPacket;
import mod.syconn.swm.network.packets.ToggleLightsaberPacket;

import java.util.Collections;

public class Network {

    public static void registerReceivers() {
        registerCommonReceivers();

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SyncResourceDataPacket.TYPE, SyncResourceDataPacket.STREAM_CODEC, SyncResourceDataPacket::handle);
    }

    public static void registerServerPackets() {
        registerCommonReceivers();

        NetworkManager.registerS2CPayloadType(SyncResourceDataPacket.TYPE, SyncResourceDataPacket.STREAM_CODEC, Collections.emptyList());
    }

    private static void registerCommonReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ToggleLightsaberPacket.TYPE, ToggleLightsaberPacket.STREAM_CODEC, ToggleLightsaberPacket::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ThrowLightsaberPacket.TYPE, ThrowLightsaberPacket.STREAM_CODEC, ThrowLightsaberPacket::handle);
    }
}
