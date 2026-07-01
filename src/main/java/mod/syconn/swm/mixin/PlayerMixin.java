package mod.syconn.swm.mixin;

import mod.syconn.swm.registry.ModItems;
import mod.syconn.swm.registry.ModSounds;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.ItemStackUtil;
import mod.syconn.swm.utils.interfaces.IItemExtensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    @Shadow @Final
    private Inventory inventory;

    @Unique
    private ItemStack swm$previousStackRef;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public SWGear swm$getSWGear() {
        if (inventory instanceof SWGear.SWGearAccess access) return access.swm$getSWGear();
        return null;
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (this.swm$getSWGear() != null) compound.put(Constants.MOD + ":swGear", this.swm$getSWGear().save());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compound, CallbackInfo info) {
        if (compound.contains(Constants.MOD + ":swGear")) this.swm$getSWGear().load(compound.getCompound(Constants.MOD + ":swGear"));
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 3)
    public boolean sweepingAttackLightsabers(boolean value) {
        return this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem || value;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 4)
    public SoundEvent modifySweepAttack(SoundEvent sound) {
        if (this.getMainHandItem().is(ModItems.LIGHTSABER.get()) && LightsaberTag.getOrCreate(this.getMainHandItem()).isActive()) return ModSounds.LIGHTSABER_SWING.get();
        return sound;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getMainHandItem()Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.BEFORE))
    private void onTick(CallbackInfo ci) {
        var self = (Player)(Object)this;
        var inv = self.getInventory();

        var previousStackSlot = ItemStackUtil.getSlotWithStack(inv, swm$previousStackRef);

        if (previousStackSlot == -1 || previousStackSlot == inv.selected) {
            swm$previousStackRef = self.getMainHandItem();
            return;
        }

        var prevStack = swm$previousStackRef;
        var currentStack = self.getMainHandItem();

        if (currentStack.getItem() instanceof IItemExtensions listener && listener.onItemSelected(self, currentStack)) self.getInventory().setItem(inv.selected, currentStack);

        if (prevStack.getItem() instanceof IItemExtensions listener && listener.onItemDeselected(self, prevStack)) self.getInventory().setItem(previousStackSlot, prevStack);
        swm$previousStackRef = self.getMainHandItem();
    }
}
