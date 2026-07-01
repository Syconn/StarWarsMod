package mod.syconn.swm.api.registry.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface BlockEntityRendererRegister {
    <T extends BlockEntity> void apply(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> provider);
}
