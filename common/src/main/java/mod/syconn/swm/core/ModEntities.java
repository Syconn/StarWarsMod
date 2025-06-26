package mod.syconn.swm.core;

import dev.architectury.registry.registries.DeferredRegister;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaber;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Constants.MOD, Registry.ENTITY_TYPE_REGISTRY);

    public static final Supplier<EntityType<ThrownLightsaber>> THROWN_LIGHTSABER = registerProjectile("throw_lightsaber", ThrownLightsaber::new);

    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, MobCategory mobCategory) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity,mobCategory).sized(width, height).build(name));
    }

    private static <T extends Projectile> Supplier<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> entity) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity, MobCategory.MISC).sized(.5f, .5f).clientTrackingRange(4).updateInterval(10).build(name));
    }
}
