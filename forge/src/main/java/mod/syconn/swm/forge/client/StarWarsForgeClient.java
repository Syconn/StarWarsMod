package mod.syconn.swm.forge.client;

import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.client.render.entity.layers.LightsaberLayer;
import mod.syconn.swm.utils.Constants;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class StarWarsForgeClient {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) StarWarsClient.onClientTick(event.player);
    }

    @SubscribeEvent
    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
        addPlayerLayers(event.getPlayerSkin("default"));
        addPlayerLayers(event.getPlayerSkin("slim"));
    }

    private static void addPlayerLayers(EntityRenderer<? extends Player> renderer) {
        if(renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new LightsaberLayer<>(playerRenderer));
    }
}
