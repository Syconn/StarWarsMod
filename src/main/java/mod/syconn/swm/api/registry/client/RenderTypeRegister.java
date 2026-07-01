package mod.syconn.swm.api.registry.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface RenderTypeRegister {
    void apply(Block block, RenderType type);
}
