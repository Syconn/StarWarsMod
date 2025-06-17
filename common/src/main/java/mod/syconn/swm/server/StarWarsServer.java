package mod.syconn.swm.server;

import dev.architectury.event.events.common.PlayerEvent;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.util.server.SyncedResourceManager;
import net.minecraft.server.level.ServerPlayer;

public class StarWarsServer {

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);

        Network.registerServerPackets();
    }

    public static void playerJoinedServer(ServerPlayer player) {
        SyncedResourceManager.handleJoin(player);
    }
}
