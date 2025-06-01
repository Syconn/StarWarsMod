package mod.syconn.swm.forge.client;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StarWarsForgeClient {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        StarWarsClient.init();
    }
}
