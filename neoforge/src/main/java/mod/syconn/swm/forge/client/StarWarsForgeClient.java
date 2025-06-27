package mod.syconn.swm.forge.client;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.util.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class StarWarsForgeClient {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) StarWarsClient.onClientTick(event.player);
    }
}
