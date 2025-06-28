package mod.syconn.swm;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import mod.syconn.swm.core.*;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.server.StarWarsServer;
import mod.syconn.swm.util.server.SyncedResourceManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.Executor;

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

        SyncedResourceManager.register(LightsaberContent.LIGHTSABER_DATA);

        PlayerEvent.PLAYER_JOIN.register(StarWarsServer::playerJoinedServer);

        ReloadListenerRegistry.register(PackType.SERVER_DATA, LightsaberContent.LIGHTSABER_DATA);
    }
}
