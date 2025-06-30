package mod.syconn.swm.features.lightsaber.entity;

import dev.architectury.hooks.item.ItemStackHooks;
import mod.syconn.swm.core.ModDamageSources;
import mod.syconn.swm.core.ModEntities;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.utils.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownLightsaber extends ThrowableProjectile {

    private static final EntityDataAccessor<CompoundTag> LIGHTSABER_DATA = SynchedEntityData.defineId(ThrownLightsaber.class, EntityDataSerializers.COMPOUND_TAG);
    private InteractionHand hand;
    private boolean returning = false;

    public ThrownLightsaber(EntityType<? extends ThrownLightsaber> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownLightsaber(Level level, LivingEntity shooter, ItemStack stack, InteractionHand hand) {
        super(ModEntities.THROWN_LIGHTSABER.get(), shooter, level);
        this.entityData.set(LIGHTSABER_DATA, LightsaberTag.getOrCreate(stack).save());
        this.hand = hand;
    }

    public ThrownLightsaber(Level level, LivingEntity shooter, InteractionHand hand) {
        super(ModEntities.THROWN_LIGHTSABER.get(), shooter, level);
        this.entityData.set(LIGHTSABER_DATA, LightsaberTag.getOrCreate(shooter.getItemInHand(hand)).save());
        this.hand = hand;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIGHTSABER_DATA, new CompoundTag());
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
        return LightsaberTag.getTemporary(new LightsaberTag(entityData.get(LIGHTSABER_DATA)), true);
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

            if (entity instanceof LivingEntity livingEntity2) {
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity2, entity2);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity2, livingEntity2);
                }
            }
        }

        this.playSound(soundEvent, 1.0F, 1.0F);
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
