package mod.syconn.swm.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.network.CraftHiltPacket;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.packets.SyncResourceDataPacket;
import mod.syconn.swm.util.Constants;

public class Network {

    public static NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(ToggleLightsaberPacket.class, ToggleLightsaberPacket::encode, ToggleLightsaberPacket::new, ToggleLightsaberPacket::apply);
        CHANNEL.register(ThrowLightsaberPacket.class, ThrowLightsaberPacket::encode, ThrowLightsaberPacket::new, ThrowLightsaberPacket::apply);
        CHANNEL.register(SyncResourceDataPacket.class, SyncResourceDataPacket::encode, SyncResourceDataPacket::new, SyncResourceDataPacket::apply);
        CHANNEL.register(ChangeLightsaberHSVPacket.class, ChangeLightsaberHSVPacket::encode, ChangeLightsaberHSVPacket::new, ChangeLightsaberHSVPacket::apply);
        CHANNEL.register(CraftHiltPacket.class, CraftHiltPacket::encode, CraftHiltPacket::new, CraftHiltPacket::apply);
    }
}
