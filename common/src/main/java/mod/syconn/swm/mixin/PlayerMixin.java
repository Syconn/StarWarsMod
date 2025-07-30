package mod.syconn.swm.mixin;

import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.utils.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements SWGear.SWGearAccess {

    @Unique
    private final static EntityDataAccessor<CompoundTag> SW_GEAR = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

    @Shadow @Final private Inventory inventory;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public SWGear swm$getSWGear() {
        return this.inventory.swm$getSWGear();
    }

    @Override
    public void swm$setSyncedData(SWGear swGear) {
        this.entityData.set(SW_GEAR, swGear.save());
    }

    @Override
    public SWGear swm$getSyncedData() {
        var temp = new SWGear(((Player) (Object) this).getInventory());
        temp.load(this.entityData.get(SW_GEAR));
        return temp;
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineData(CallbackInfo ci) {
        this.entityData.define(SW_GEAR, new CompoundTag());
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (this.swm$getSWGear() != null) compound.put(Constants.MOD + ":swGear", this.swm$getSWGear().save());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compound, CallbackInfo info) {
        if (compound.contains(Constants.MOD + ":swGear")) {
            this.swm$getSWGear().load(compound.getCompound(Constants.MOD + ":swGear"));
            this.swm$setSyncedData(this.swm$getSWGear());
        }
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 3)
    public boolean sweepingAttackLightsabers(boolean value) {
        return this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem || value;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 4)
    public SoundEvent modifySweepAttack(SoundEvent sound) {
        if (this.getMainHandItem().is(ModItems.LIGHTSABER.get())) return ModSounds.LIGHTSABER_SWING.get();
        return sound;
    }
}
