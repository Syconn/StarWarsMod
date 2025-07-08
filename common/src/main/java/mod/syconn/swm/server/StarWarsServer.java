package mod.syconn.swm.server;

import dev.architectury.event.events.common.PlayerEvent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.client.HologramData;
import mod.syconn.swm.utils.server.SyncedResourceManager;
import net.minecraft.server.level.ServerPlayer;

public class StarWarsServer {

    public static void init() {
        SyncedResourceManager.register(LightsaberContent.LIGHTSABER_DATA);

        PlayerEvent.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);
        PlayerEvent.PLAYER_QUIT.register(StarWarsServer::playerLeaveServer);
    }

    public static void playerJoinedServer(ServerPlayer player) {
        SyncedResourceManager.handleJoin(player);
    }

    public static void playerLeaveServer(ServerPlayer player) {
        HologramNetwork.get(player.serverLevel()).playerLeave(player);
    }
}
