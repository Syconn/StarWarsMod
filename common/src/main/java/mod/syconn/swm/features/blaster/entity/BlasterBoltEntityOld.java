package mod.syconn.swm.features.blaster.entity;

import mod.syconn.swm.core.ModDamageSources;
import mod.syconn.swm.core.ModEntities;
import mod.syconn.swm.features.blaster.server.data.MuzzleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BlasterBoltEntityOld extends ThrowableProjectile {

    private static final EntityDataAccessor<CompoundTag> BLASTER_DATA = SynchedEntityData.defineId(BlasterBoltEntityOld.class, EntityDataSerializers.COMPOUND_TAG);

    public BlasterBoltEntityOld(EntityType<? extends BlasterBoltEntityOld> entityType, Level level) {
        super(entityType, level);
    }

    public BlasterBoltEntityOld(Level level, CompoundTag tag) {
        super(ModEntities.BLASTER_BOLT.get(), level);
        this.entityData.set(BLASTER_DATA, tag);
    }

    public BlasterBoltEntityOld(LivingEntity entity, CompoundTag tag) {
        super(ModEntities.BLASTER_BOLT.get(), entity, entity.level());
        this.entityData.set(BLASTER_DATA, tag);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BLASTER_DATA, new CompoundTag());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        var entity = pResult.getEntity();
        var damageSource = ModDamageSources.blaster(level(), this, this.getOwner());

        if (entity instanceof LivingEntity livingEntity && livingEntity.isDamageSourceBlocked(damageSource)) deflect((Player) entity);
        else if (!entity.is(getOwner()) && entity.hurt(damageSource, 4.0f)) {
            if (entity.getType() == EntityType.ENDERMAN) return;

            if (entity instanceof LivingEntity livingEntity) {
                if (getOwner() instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, getOwner());
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) getOwner(), livingEntity);
                }
            }
        }

        this.discard();
    }

    private void deflect(Player player) {
        var bolt = new BlasterBoltEntityOld(level(), this.entityData.get(BLASTER_DATA));
        bolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F); // TODO MODIFY FOR BLADE ACCURACY
        level().addFreshEntity(bolt);
    }

    private MuzzleData blasterData() {
        return new MuzzleData(this.entityData.get(BLASTER_DATA));
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return super.shouldRender(x, y, z);
    }
}
