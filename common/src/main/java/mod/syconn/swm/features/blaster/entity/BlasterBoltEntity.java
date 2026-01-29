package mod.syconn.swm.features.blaster.entity;

import dev.architectury.extensions.network.EntitySpawnExtension;
import dev.architectury.networking.NetworkManager;
import dev.kosmx.playerAnim.core.util.Vec3d;
import mod.syconn.swm.core.ModDamageSources;
import mod.syconn.swm.core.ModParticles;
import mod.syconn.swm.core.ModTags;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.clientside.ScorchBlockPacket;
import mod.syconn.swm.utils.generic.MathUtil;
import mod.syconn.swm.utils.interfaces.IPrecisionVelocityEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class BlasterBoltEntity extends ThrowableProjectile implements IPrecisionVelocityEntity, EntitySpawnExtension {

    private static final EntityDataAccessor<Integer> LIFE = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ODOMETER = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> LENGTH = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> SMOLDERING = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> ARM = SynchedEntityData.defineId(BlasterBoltEntity.class, EntityDataSerializers.BYTE);

    private boolean ignoreWater;
    private Function<Double, Double> damageFunction;

    @Environment(EnvType.CLIENT)
    public Vec3 sourceOffset;

    public BlasterBoltEntity(EntityType<? extends BlasterBoltEntity> type, Level level) {
        super(type, level);
    }

    public BlasterBoltEntity(EntityType<? extends BlasterBoltEntity> type, LivingEntity owner, Level level, boolean ignoreWater) {
        super(type, owner, level);
        this.ignoreWater = ignoreWater;
    }

    public void setSourceArm(HumanoidArm arm) {
        this.entityData.set(ARM, (byte)arm.getId());
    }

    public Optional<HumanoidArm> getSourceArm() {
        var arm = this.entityData.get(ARM);
        return deserializeArm(arm);
    }

    @NotNull
    private static Optional<HumanoidArm> deserializeArm(byte arm) {
        return switch (arm) {
            case 0 -> Optional.of(HumanoidArm.LEFT);
            case 1 -> Optional.of(HumanoidArm.RIGHT);
            default -> Optional.empty();
        };
    }

    protected boolean shouldCreateScorch()
    {
        return true;
    }

    protected boolean shouldDestroyBlocks()
    {
        return true;
    }

    public void setRange(float range) {
        var ticksToLive = (int)(range / getDeltaMovement().length());
        setLife(ticksToLive);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    @Override
    public void saveAdditionalSpawnData(FriendlyByteBuf buf) {
        buf.writeNbt(writeSpawnData(new CompoundTag()));
    }

    @Override
    public void loadAdditionalSpawnData(FriendlyByteBuf buf) {
        this.readSpawnData(Objects.requireNonNull(buf.readNbt()));
    }

    public CompoundTag writeSpawnData(CompoundTag tag) {
        tag.putInt("life", getLife());
        tag.putBoolean("ignoreWater", ignoreWater);
        tag.putInt("color", getColor());
        tag.putFloat("length", getLength());
        tag.putFloat("radius", getRadius());
        tag.putFloat("odometer", getOdometer());
        tag.putBoolean("smoldering", isSmoldering());
        tag.putByte("arm", (byte)getSourceArm().orElse(HumanoidArm.LEFT).getId());
        return tag;
    }

    public void readSpawnData(CompoundTag tag) {
        setLife(tag.getInt("life"));
        ignoreWater = tag.getBoolean("ignoreWater");
        setColor(tag.getInt("color"));
        setLength(tag.getFloat("length"));
        setRadius(tag.getFloat("radius"));
        setOdometer(tag.getFloat("odometer"));
        setSmoldering(tag.getBoolean("smoldering"));
        setSourceArm(deserializeArm(tag.getByte("arm")).orElse(HumanoidArm.RIGHT));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        writeSpawnData(compound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        readSpawnData(compound);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFE, 100);
        this.entityData.define(COLOR, 0);
        this.entityData.define(LENGTH, 1f);
        this.entityData.define(ODOMETER, 0f);
        this.entityData.define(RADIUS, 1f);
        this.entityData.define(SMOLDERING, false);
        this.entityData.define(ARM, (byte)255);
    }

    private int getLife() {
        return this.entityData.get(LIFE);
    }

    private void setLife(int life) {
        this.entityData.set(LIFE, life);
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(COLOR, color);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(float radius) {
        this.entityData.set(RADIUS, radius);
    }

    public float getOdometer() {
        return this.entityData.get(ODOMETER);
    }

    public void setOdometer(float odometer) {
        this.entityData.set(ODOMETER, odometer);
    }

    public float getLength() {
        return this.entityData.get(LENGTH);
    }

    public void setLength(float length) {
        this.entityData.set(LENGTH, length);
    }

    public boolean isSmoldering() {
        return this.entityData.get(SMOLDERING);
    }

    public void setSmoldering(boolean smoldering) {
        this.entityData.set(SMOLDERING, smoldering);
    }

    @Override
    public void tick() {
        if (!level().isClientSide && this.tickCount > this.getLife()) {
            this.discard();
            return;
        }

        if (level().isClientSide && this.tickCount > 1 && isSmoldering()) {
            var vec = position();
            var vel = getDeltaMovement();
            var n = 10;
            var dVel = vel.scale(1f / n);

            for (var i = 0; i < n; i++) {
                var dx = 0.01 * level().random.nextGaussian();
                var dy = 0.01 * level().random.nextGaussian();
                var dz = 0.01 * level().random.nextGaussian();

                level().addParticle(ModParticles.SLUG_TRAIL.get(), vec.x, vec.y, vec.z, dx, dy, dz);

                vec = vec.add(dVel);
            }
        }

        var dist = getOdometer();
        dist += (float) this.getDeltaMovement().length();
        setOdometer(dist);

        super.tick();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() != HitResult.Type.MISS) this.setPos(hitResult.getLocation());

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            var blockHit = (BlockHitResult)hitResult;

            var blockPos = blockHit.getBlockPos();
            var shouldScorch = true;

            var state = level().getBlockState(blockPos);

            if (state.is(ModTags.BLASTER_DEFLECTION) && deflect(blockHit, state)) return;

            if (shouldDestroyBlocks()) {
                if (!this.level().isClientSide) {
                    if (state.is(ModTags.BLASTER_DESTROY)) {
                        level().destroyBlock(blockPos, false, this);
                        shouldScorch = false;
                    } else if (state.is(ModTags.BLASTER_EXPLODE)) {
                        level().destroyBlock(blockPos, false, this);
                        if (state.getBlock() instanceof TntBlock) {
                            PrimedTnt tntEntity = new PrimedTnt(level(), (double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5, null);
                            tntEntity.setFuse(0);
                            level().addFreshEntity(tntEntity);
                        } else this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), 4.0F, Level.ExplosionInteraction.BLOCK);

                        shouldScorch = false;
                    }
                }
            }

            if (shouldCreateScorch() && shouldScorch) {
                if (!this.level().isClientSide) {
                    if (level().isWaterAt(blockPos) && ignoreWater) return;

                    var incident = this.getDeltaMovement().normalize();
                    var normal = new Vec3(blockHit.getDirection().step());
                    var pos = hitResult.getLocation();

                    for (var trackingPlayer : Network.tracking((ServerLevel) level(), new ChunkPos(blockHit.getBlockPos()))) Network.CHANNEL.sendToPlayer(trackingPlayer, new ScorchBlockPacket(pos, incident, normal, true));
                }
            }
        }
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            var entityHit = (EntityHitResult)hitResult;
            var entity = entityHit.getEntity();

            if (entity instanceof LivingEntity le && le.getUseItem().getItem() instanceof LightsaberItem && le.isUsingItem()) {
                if (deflect(le)) return;
            } else damage(entity);
        }

        this.discard();
    }

    protected boolean deflect(LivingEntity entity) {
        var speed = this.getDeltaMovement().length();

        var yaw = entity.getXRot();
        var pitch = entity.getYRot();

        float x = -Mth.sin(yaw * Mth.RAD_TO_DEG) * Mth.cos(pitch * Mth.RAD_TO_DEG);
        float y = -Mth.sin(pitch * Mth.RAD_TO_DEG);
        float z = Mth.cos(yaw * Mth.RAD_TO_DEG) * Mth.cos(pitch * Mth.RAD_TO_DEG);

        this.setXRot(yaw);
        this.setYRot(pitch);
        this.setDeltaMovement(x * speed, y * speed, z * speed);
        this.hurtMarked = true;

        return true;
    }

    protected boolean deflect(BlockHitResult hit, BlockState state) {
        var velocity = this.getDeltaMovement();
        var dir = velocity.normalize();

        var normal = new Vec3(hit.getDirection().step());
        var newDir = MathUtil.reflect(dir, normal);

        // TODO: decrease damage on reflection?
        this.setDeltaMovement(newDir.scale(velocity.length()));
        this.hurtMarked = true;

        return true;
    }

    protected void damage(Entity target) {
        if (damageFunction == null || !getTargetedEntityClass().isAssignableFrom(target.getClass())) return;
        target.hurt(ModDamageSources.blaster(level(), this, this.getOwner()), (float)(double)damageFunction.apply((double)getOdometer()));
    }

    private static Class<? extends Entity> getTargetedEntityClass() {
//        var config = Resources.CONFIG.get(); TODO CONFIG HERE
//        if (config.server.allowBlasterNonlivingDamage) return Entity.class;
        return LivingEntity.class;
    }

    public void setDamageFunction(Function<Double, Double> damage) {
        this.damageFunction = damage;
    }
}
