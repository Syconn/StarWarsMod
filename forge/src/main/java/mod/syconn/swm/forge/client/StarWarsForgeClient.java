package mod.syconn.swm.forge.client;

import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.client.render.entity.layers.SWGearLayer;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.FileUtil;
import mod.syconn.swm.utils.interfaces.ISpecialRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StarWarsForgeClient {

    @SubscribeEvent
    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
        addPlayerLayers(event.getPlayerSkin("default"), event.getContext());
        addPlayerLayers(event.getPlayerSkin("slim"), event.getContext());
    }

    @SubscribeEvent
    public static void registerAdditionalModels(final ModelEvent.RegisterAdditional event) {
        var files = new ArrayList<ResourceLocation>();
        ISpecialRenderer.SPECIAL_RENDER_FOLDER.forEach(path -> FileUtil.scanFilesInDirectory(path, ISpecialRenderer::itemModelPath, files));
        files.addAll(ISpecialRenderer.SPECIAL_RENDERS);
        files.forEach(event::register);
        event.register(Constants.withId("lightsaber/mace", "inventory"));
    }

    private static void addPlayerLayers(EntityRenderer<? extends Player> renderer, EntityRendererProvider.Context context) {
        if(renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new SWGearLayer<>(playerRenderer, context.getItemRenderer()));
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    static class StarWarsForgeModClient {

        @SubscribeEvent
        public static void clientTickEvent(TickEvent.PlayerTickEvent event) {
            if (event.side.isClient()) StarWarsClient.onClientTick(event.player);
        }

        @SubscribeEvent
        public static void renderAbstractScreen(ContainerScreenEvent.Render.Background event) {
            ClientHooks.overrideAbstractScreen(event.getContainerScreen(), event.getGuiGraphics(),  event.getContainerScreen().getGuiLeft(), event.getContainerScreen().getGuiTop());
        }
    }
}
