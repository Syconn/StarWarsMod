package mod.syconn.swm.api.registry.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface EntityRendererRegister {
    <E extends Entity> void apply(EntityType<? extends E> entityType, EntityRendererProvider<E> entityRendererFactory);
}
