package mod.syconn.swm.features.blaster;

import mod.syconn.swm.registry.ModEntities;
import mod.syconn.swm.registry.ModParticles;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import mod.syconn.swm.features.blaster.entity.BlasterIonBoltEntity;
import mod.syconn.swm.features.blaster.entity.BlasterStunBoltEntity;
import mod.syconn.swm.utils.generic.EntityUtil;
import mod.syconn.swm.utils.generic.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Function;

public class BlasterUtil {

    public static void fireBolt(Level level, Player player, float range, Function<Double, Double> damage, boolean ignoreWater, Consumer<BlasterBoltEntity> entityInitializer) {
        final var bolt = new BlasterBoltEntity(ModEntities.BLASTER_BOLT.get(), player, level, ignoreWater);
        entityInitializer.accept(bolt);
        bolt.setRange(range);
        bolt.setDamageFunction(damage);

        level.addFreshEntity(bolt);
    }

    public static void reflect(Level level, LivingEntity player, Function<Double, Double> damage, boolean ignoreWater, Consumer<BlasterBoltEntity> entityInitializer) {
        final var bolt = new BlasterBoltEntity(ModEntities.BLASTER_BOLT.get(), player, level, ignoreWater);
        entityInitializer.accept(bolt);
        bolt.setDamageFunction(damage);

        level.addFreshEntity(bolt);
    }

    public static void fireIon(Level level, Player player, float range, boolean ignoreWater, Consumer<BlasterBoltEntity> entityInitializer) {
        final var bolt = new BlasterIonBoltEntity(ModEntities.BLASTER_ION_BOLT.get(), player, level, ignoreWater);
        entityInitializer.accept(bolt);
        bolt.setRange(range);

        // TODO: ion effects

        level.addFreshEntity(bolt);
    }

    public static void fireStun(Level level, Player player, Vec3 fromDir, float range, boolean ignoreWater, Consumer<BlasterBoltEntity> entityInitializer) {
        final var bolt = new BlasterStunBoltEntity(ModEntities.BLASTER_STUN_BOLT.get(), player, level, ignoreWater);
        entityInitializer.accept(bolt);
        bolt.setRange(range);

        level.addFreshEntity(bolt);

        var start = new Vec3(bolt.getX(), bolt.getY() + bolt.getBbHeight() / 2f, bolt.getZ());

        var hit = EntityUtil.raycastEntitiesCone(start, fromDir, 10 / 180f * Math.PI, range, player, new Entity[] { player });

        // TODO: prevent stunning through walls
        // TODO: ignore water

        for (var e : hit) {
            if (e instanceof LivingEntity le) {
                le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 3, true, false), player);
                le.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 7 * 20, 0, true, false), player);
                le.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 4 * 20, 0, true, false), player);
                le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 6 * 20, 1, true, false), player);
            }
        }
    }

    public static void createScorchParticles(Level level, Vec3 pos, Vec3 incident, Vec3 normal, boolean energyScorch) {
        var blockPos = new BlockPos(MathUtil.floorInt(pos.subtract(normal.scale(0.1f))));

        assert level != null;

        var offset = 0.005 + 0.0005 * level.random.nextDouble();
        var heatEncodedNormal = normal.scale(energyScorch ? 1 : 0.3f);
        level.addParticle(ModParticles.SCORCH.get(), pos.x + normal.x * offset, pos.y + normal.y * offset, pos.z + normal.z * offset, heatEncodedNormal.x, heatEncodedNormal.y, heatEncodedNormal.z);

        var reflection = MathUtil.reflect(incident, normal);

        for (var i = 0; i < 16; i++) {
            var vx = level.random.nextGaussian() * 0.03;
            var vy = level.random.nextGaussian() * 0.03;
            var vz = level.random.nextGaussian() * 0.03;

            var sparkVelocity = reflection.scale(0.3f * (level.random.nextDouble() * 0.5 + 0.5));
            level.addParticle(ModParticles.SPARK.get(), pos.x, pos.y, pos.z, sparkVelocity.x + vx, sparkVelocity.y + vy, sparkVelocity.z + vz);

            if (i % 3 == 0) {
                var debrisVelocity = reflection.scale(0.15f * (level.random.nextDouble() * 0.5 + 0.5));
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockPos)), pos.x, pos.y, pos.z, debrisVelocity.x + vx, debrisVelocity.y + vy, debrisVelocity.z + vz);
            }

            if (i % 2 == 0) {
                var smokeVelocity = reflection.scale(0.08f * (level.random.nextDouble() * 0.5 + 0.5));
                level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, smokeVelocity.x + vx, smokeVelocity.y + vy, smokeVelocity.z + vz);
            }
        }
    }
}
