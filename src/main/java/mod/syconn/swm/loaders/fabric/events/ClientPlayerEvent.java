package mod.syconn.swm.loaders.fabric.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.LocalPlayer;

public class ClientPlayerEvent {
    public static Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createArrayBacked(ClientPlayerJoin.class, listeners -> player -> {
        for (ClientPlayerJoin callback : listeners) callback.join(player);
    });

    @Environment(EnvType.CLIENT)
    public interface ClientPlayerJoin {
        void join(LocalPlayer player);
    }
}
