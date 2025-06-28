package mod.syconn.swm.server;

import mod.syconn.swm.network.Network;
import mod.syconn.swm.util.server.SyncedResourceManager;
import net.minecraft.server.level.ServerPlayer;

public class StarWarsServer {

    public static void init() {
//        SyncedResourceManager.register(LightsaberContent.LIGHTSABER_DATA);
//
//        PlayerEvent.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);

        Network.registerServerPackets();
    }

    public static void playerJoinedServer(ServerPlayer player) {
        SyncedResourceManager.handleJoin(player);
    }
}
