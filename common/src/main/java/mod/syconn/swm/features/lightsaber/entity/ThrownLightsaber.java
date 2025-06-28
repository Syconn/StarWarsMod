package mod.syconn.swm.features.lightsaber.entity;

import dev.architectury.hooks.item.ItemStackHooks;
import mod.syconn.swm.core.ModDamageSources;
import mod.syconn.swm.core.ModEntities;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import mod.syconn.swm.util.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownLightsaber extends ThrowableProjectile {

    private static final EntityDataAccessor<ItemStack> LIGHTSABER = SynchedEntityData.defineId(ThrownLightsaber.class, EntityDataSerializers.ITEM_STACK);
    private InteractionHand hand;
    private boolean returning = false;

    public ThrownLightsaber(EntityType<? extends ThrownLightsaber> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownLightsaber(Level level, LivingEntity shooter, ItemStack stack, InteractionHand hand) {
        super(ModEntities.THROWN_LIGHTSABER.get(), shooter, level);
        this.entityData.set(LIGHTSABER, stack);
        this.hand = hand;
    }

    public ThrownLightsaber(Level level, LivingEntity shooter, InteractionHand hand) {
        super(ModEntities.THROWN_LIGHTSABER.get(), shooter, level);
        this.entityData.set(LIGHTSABER, shooter.getItemInHand(hand).copy());
        this.hand = hand;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIGHTSABER, new ItemStack(ModItems.LIGHTSABER));
    }

    @Override
    public void tick() {
        var entity = this.getOwner();

        if (this.getOwner() != null) {
            if (this.distanceTo(getOwner()) >= 8) this.returning = true;

            if (this.distanceTo(getOwner()) <= 2.5f && !this.level().isClientSide && returning) {
                if (this.getOwner() instanceof Player player && !player.isCreative()) {
                    if (player.getItemInHand(hand).isEmpty()) player.setItemSlot(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, this.getItem());
                    else if (player instanceof ServerPlayer sp) ItemStackHooks.giveItem(sp, this.getItem());
                }
                this.discard();
            }
        }

        if (this.isInWater()) this.returning = true;

        if ((this.returning) && entity != null) {
            var vec3 = entity.getEyePosition().subtract(0, 0.25d, 0).subtract(this.position());
            var returnLevel = 2;
            this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * returnLevel, this.getZ());
            if (this.level().isClientSide) this.yOld = this.getY();

            var d = 0.05 * returnLevel;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d)));
        }

        super.tick();
    }

    public ItemStack getItem() {
        return LightsaberComponent.update(this.entityData.get(LIGHTSABER), LightsaberComponent::activate);
    }

    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        var entity = result.getEntity();
        var f = 8.0F;

        var entity2 = this.getOwner();
        var damageSource = ModDamageSources.lightsaber(level());
        var soundEvent = SoundEvents.TRIDENT_HIT; // TODO CHANGE THIS TOO
        if (entity2 != entity && entity.hurt(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) return;
            if (this.level() instanceof ServerLevel serverLevel) EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, this.getWeaponItem());
            if (entity instanceof LivingEntity livingEntity) this.doKnockback(livingEntity, damageSource);
        }

        this.playSound(soundEvent, 1.0F, 1.0F);
    }

    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        var d = this.level() instanceof ServerLevel serverLevel ? EnchantmentHelper.modifyKnockback(serverLevel, getItem(), entity, damageSource, 0.0F) : 0.0F;
        if (d > 0.0) {
            var e = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            var vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(d * 0.6 * e);
            if (vec3.lengthSqr() > 0.0) entity.push(vec3.x, 0.1, vec3.z);
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.returning = true;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) super.playerTouch(player);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.returning = compound.getBoolean("return");
        this.hand = NbtTools.getEnum(InteractionHand.class, compound.getCompound("hand"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("return", this.returning);
        compound.put("hand", NbtTools.writeEnum(this.hand));
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
