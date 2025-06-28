package mod.syconn.swm.neoforge;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.server.StarWarsServer;
import mod.syconn.swm.util.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod(Constants.MOD)
public final class StarWarsForge {

    public StarWarsForge() {
        StarWars.init();
    }

    @EventBusSubscriber(modid = Constants.MOD, bus = EventBusSubscriber.Bus.MOD)
    public static class StarWarsServerForge {

        @SubscribeEvent
        public static void init(FMLDedicatedServerSetupEvent event) {
            StarWarsServer.init();
        }
    }

    @EventBusSubscriber(modid = Constants.MOD, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class StarWarsClientForge {

        @SubscribeEvent
        public static void init(FMLClientSetupEvent event) {
            StarWarsClient.init();
        }
    }
}
