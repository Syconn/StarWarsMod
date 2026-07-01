package mod.syconn.swm.api.registry.client;

import mod.syconn.swm.api.util.IHudOverlay;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface HudOverlayRegister {
    void apply(ResourceLocation id, IHudOverlay overlay);
}
