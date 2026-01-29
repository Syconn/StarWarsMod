package mod.syconn.swm;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.*;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.server.StarWarsServer;
import mod.syconn.swm.utils.Constants;
import net.minecraft.server.packs.PackType;

public final class StarWars {
    public static void init() {
        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModItems.TABS.register();
        ModSounds.SOUNDS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModEntities.ENTITIES.register();
        ModMenus.MENUS.register();
        ModParticles.PARTICLES.register();
        ModRecipes.RECIPES.register();
        ModRecipes.SERIALIZER.register();

        Network.init();

        CreativeTabRegistry.modify(ModItems.TAB, ModItems::addCreative);

        ReloadListenerRegistry.register(PackType.SERVER_DATA, LightsaberContent.LIGHTSABER_DATA, Constants.withId("lightsaber_data"));
        ReloadListenerRegistry.register(PackType.SERVER_DATA, BlasterContent.BLASTER_DATA, Constants.withId("blaster_data"));

        EnvExecutor.runInEnv(Env.CLIENT, () -> StarWarsClient::init);
        LifecycleEvent.SETUP.register(StarWarsServer::init);
    }
}
