package mod.syconn.swm.server;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.server.SyncedResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class StarWarsServer {

    public static void init() {
        SyncedResourceManager.register(LightsaberContent.LIGHTSABER_DATA);
        SyncedResourceManager.register(BlasterContent.BLASTER_DATA);

        PlayerEvent.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);
        PlayerEvent.PLAYER_QUIT.register(StarWarsServer::playerLeaveServer);
        TickEvent.SERVER_PRE.register(StarWarsServer::serverTick);
    }

    public static void playerJoinedServer(ServerPlayer player) {
        SyncedResourceManager.handleJoin(player);
        SWGear.getAndSyncGear(player);
    }

    public static void playerLeaveServer(ServerPlayer player) {
        HologramNetwork.get(player.server.overworld()).playerLeave(player);
    }

    public static void serverTick(MinecraftServer server){
        HologramNetwork.get(server.overworld()).serverTick(server.overworld());
    }
}
