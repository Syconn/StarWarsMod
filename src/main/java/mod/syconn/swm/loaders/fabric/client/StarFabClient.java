//? if fabric {
package mod.syconn.swm.loaders.fabric.client;

import mod.syconn.swm.api.registry.client.ParticleProviderRegister;
import mod.syconn.swm.api.registry.client.ScreenRegister;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.client.render.entity.layers.SWGearLayer;
import mod.syconn.swm.loaders.fabric.events.ClientPlayerEvent;
import mod.syconn.swm.loaders.fabric.events.PlayerEvents;
import mod.syconn.swm.utils.Constants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.function.TriFunction;

public class StarFabClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        StarWarsClient.init();
        StarWarsClient.registerBlockEntityRenderers(BlockEntityRenderers::register);
        StarWarsClient.registerEntityRenderers(EntityRendererRegistry::register);
        StarWarsClient.registerRenderTypes(BlockRenderLayerMap.INSTANCE::putBlock);
        StarWarsClient.registerBlockColors(ColorProviderRegistry.BLOCK::register);
        StarWarsClient.registerItemColors(ColorProviderRegistry.ITEM::register);
        StarWarsClient.registerHudOverlays((id, overlay) -> HudRenderCallback.EVENT.register(overlay::draw));
        StarWarsClient.registerScreens(new ScreenRegister() {
            public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void apply(MenuType<T> type, TriFunction<T, Inventory, Component, U> factory) {
                MenuScreens.register(type, factory::apply);
            }
        });
        StarWarsClient.registerParticleProviders(new ParticleProviderRegister() {
            public <T extends ParticleOptions> void apply(ParticleType<T> type, ParticleProviderRegister.SpriteProvider<T> provider) {
                ParticleFactoryRegistry.getInstance().register(type, provider::apply);
            }
        });

        PlayerEvents.PLAYER_TICK.register(StarWarsClient::onClientTick);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(Constants.TRACKER::clientPlayerJoined);
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((entityType, livingEntityRenderer, registrationHelper, context) -> {
            if(livingEntityRenderer instanceof PlayerRenderer renderer) registrationHelper.register(new SWGearLayer<>(renderer, context.getItemRenderer()));
        }));
    }
}
//? }