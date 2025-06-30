package mod.syconn.swm.server;

import dev.architectury.event.events.common.PlayerEvent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.utils.server.SyncedResourceManager;
import net.minecraft.server.level.ServerPlayer;

public class StarWarsServer {

    public static void init() {
        SyncedResourceManager.register(LightsaberContent.LIGHTSABER_DATA);

        PlayerEvent.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);
    }

    public static void playerJoinedServer(ServerPlayer player) {
        SyncedResourceManager.handleJoin(player);
    }
}
