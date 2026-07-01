package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import mod.syconn.swm.features.blaster.entity.BlasterIonBoltEntity;
import mod.syconn.swm.features.blaster.entity.BlasterStunBoltEntity;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaberEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;

public class ModEntities {

    public static final RegistryEntry<EntityType<ThrownLightsaberEntity>> THROWN_LIGHTSABER = registerProjectile("throw_lightsaber", ThrownLightsaberEntity::new);
    public static final RegistryEntry<EntityType<BlasterBoltEntity>> BLASTER_BOLT = registerBolt("blaster_bolt", BlasterBoltEntity::new);
    public static final RegistryEntry<EntityType<BlasterIonBoltEntity>> BLASTER_ION_BOLT = registerBolt("blaster_ion_bolt", BlasterIonBoltEntity::new);
    public static final RegistryEntry<EntityType<BlasterStunBoltEntity>> BLASTER_STUN_BOLT = registerBolt("blaster_stun_bolt", BlasterStunBoltEntity::new);

    private static <T extends Mob> RegistryEntry<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> entity, float width, float height, MobCategory mobCategory) {
        return RegistryEntry.entityType(name, () -> EntityType.Builder.of(entity,mobCategory).sized(width, height).build(name));
    }

    private static <T extends Projectile> RegistryEntry<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> entity) {
        return RegistryEntry.entityType(name, () -> EntityType.Builder.of(entity, MobCategory.MISC).sized(.5f, .5f).clientTrackingRange(20).updateInterval(10).build(name));
    }

    private static <T extends Projectile> RegistryEntry<EntityType<T>> registerBolt(String name, EntityType.EntityFactory<T> entity) {
        return RegistryEntry.entityType(name, () -> EntityType.Builder.of(entity, MobCategory.MISC).sized(.5f, .5f).clientTrackingRange(120).updateInterval(10).build(name));
    }
}
