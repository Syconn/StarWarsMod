package mod.syconn.swm;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.*;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.server.StarWarsServer;
import net.minecraft.server.packs.PackType;

public final class StarWars {
    public static void init() {
        ModComponents.DATA_COMPONENTS.register();
        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModItems.TABS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModEntities.ENTITIES.register();
        ModMenus.MENUS.register();
        ModRecipes.RECIPES.register();
        ModRecipes.SERIALIZER.register();

        CreativeTabRegistry.modify(ModItems.TAB, ModItems::addCreative);

        ReloadListenerRegistry.register(PackType.SERVER_DATA, LightsaberContent.LIGHTSABER_DATA);

        EnvExecutor.runInEnv(Env.CLIENT, () -> StarWarsClient::init);
        EnvExecutor.runInEnv(Env.SERVER, () -> StarWarsServer::init);
    }
}
