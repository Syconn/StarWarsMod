package mod.syconn.swm.forge.client;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class StarWarsForgeClient {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) StarWarsClient.onClientTick(event.player);
    }
}
