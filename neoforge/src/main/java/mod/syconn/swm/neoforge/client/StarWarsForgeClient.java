package mod.syconn.swm.neoforge.client;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.util.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Constants.MOD, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StarWarsForgeClient {

    @SubscribeEvent
    public static void clientTickEvent(PlayerTickEvent event) {
        if (event.getEntity().level().isClientSide) StarWarsClient.onClientTick(event.getEntity());
    }
}
