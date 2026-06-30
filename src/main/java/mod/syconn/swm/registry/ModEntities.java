package mod.syconn.swm.registry;

import dev.architectury.registry.registries.DeferredRegister;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import mod.syconn.swm.features.blaster.entity.BlasterIonBoltEntity;
import mod.syconn.swm.features.blaster.entity.BlasterStunBoltEntity;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaberEntity;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Constants.MOD, Registries.ENTITY_TYPE);

    public static final Supplier<EntityType<ThrownLightsaberEntity>> THROWN_LIGHTSABER = registerProjectile("throw_lightsaber", ThrownLightsaberEntity::new);

    public static final Supplier<EntityType<BlasterBoltEntity>> BLASTER_BOLT = registerBolt("blaster_bolt", BlasterBoltEntity::new);
    public static final Supplier<EntityType<BlasterIonBoltEntity>> BLASTER_ION_BOLT = registerBolt("blaster_ion_bolt", BlasterIonBoltEntity::new);
    public static final Supplier<EntityType<BlasterStunBoltEntity>> BLASTER_STUN_BOLT = registerBolt("blaster_stun_bolt", BlasterStunBoltEntity::new);

    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, MobCategory mobCategory) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity,mobCategory).sized(width, height).build(name));
    }

    private static <T extends Projectile> Supplier<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> entity) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity, MobCategory.MISC).sized(.5f, .5f).clientTrackingRange(20).updateInterval(10).build(name));
    }

    private static <T extends Projectile> Supplier<EntityType<T>> registerBolt(String name, EntityType.EntityFactory<T> entity) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity, MobCategory.MISC).sized(.5f, .5f).clientTrackingRange(120).updateInterval(10).build(name));
    }
}
