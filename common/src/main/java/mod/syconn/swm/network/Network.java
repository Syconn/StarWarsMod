package mod.syconn.swm.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.network.CraftHiltPacket;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.packets.SyncResourceDataPacket;

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
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ChangeLightsaberHSVPacket.TYPE, ChangeLightsaberHSVPacket.STREAM_CODEC, ChangeLightsaberHSVPacket::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, CraftHiltPacket.TYPE, CraftHiltPacket.STREAM_CODEC, CraftHiltPacket::handle);
    }
}
