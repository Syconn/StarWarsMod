package mod.syconn.swm.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.swm.features.lightsaber.network.ChangeLightsaberHSVPacket;
import mod.syconn.swm.features.lightsaber.network.CraftHiltPacket;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.packets.clientside.NotifyPlayerPacket;
import mod.syconn.swm.network.packets.clientside.RequestedHologramPacket;
import mod.syconn.swm.network.packets.serverside.HoloCallPacket;
import mod.syconn.swm.network.packets.clientside.SyncResourceDataPacket;
import mod.syconn.swm.network.packets.serverside.RequestHologramPacket;
import mod.syconn.swm.utils.Constants;

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
        CHANNEL.register(NotifyPlayerPacket.class, NotifyPlayerPacket::encode, NotifyPlayerPacket::new, NotifyPlayerPacket::apply);
    }
}
