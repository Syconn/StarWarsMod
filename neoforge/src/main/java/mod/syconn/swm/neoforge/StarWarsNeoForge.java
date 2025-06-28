package mod.syconn.swm.neoforge;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.ModMenus;
import mod.syconn.swm.features.lightsaber.client.screen.LightsaberAssemblerScreen;
import mod.syconn.swm.features.lightsaber.client.screen.LightsaberWorkbenchScreen;
import mod.syconn.swm.server.StarWarsServer;
import mod.syconn.swm.util.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(Constants.MOD)
public final class StarWarsNeoForge {

    public StarWarsNeoForge() {
        StarWars.init();
    }

    @EventBusSubscriber(modid = Constants.MOD, bus = EventBusSubscriber.Bus.MOD)
    public static class StarWarsServerNeoForge {

        @SubscribeEvent
        public static void init(FMLDedicatedServerSetupEvent event) {
            StarWarsServer.init();
        }
    }

    @EventBusSubscriber(modid = Constants.MOD, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class StarWarsClientNeoForge {

        @SubscribeEvent
        public static void init(FMLClientSetupEvent event) {
            StarWarsClient.init();
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenus.LIGHTSABER_ASSEMBLER.get(), LightsaberAssemblerScreen::new);
            event.register(ModMenus.LIGHTSABER_WORKBENCH.get(), LightsaberWorkbenchScreen::new);
        }
    }
}
