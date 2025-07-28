package mod.syconn.swm.mixin;

import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.sound.LightsaberAudio;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow private @Nullable DamageSource lastDamageSource;
    @Shadow private long lastDamageStamp;
    @Unique
    private final LivingEntity starWarsMod$LivingEntity = (LivingEntity) (Object) this;

    @Inject(method = "isDamageSourceBlocked", at = @At(value = "HEAD"), cancellable = true)
    public void isDamageSourceBlocked(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        var livingEntity = (LivingEntity) (Object) this;

        if (!damageSource.is(DamageTypeTags.BYPASSES_SHIELD) && livingEntity.getUseItem().getItem() instanceof LightsaberItem) {
            var lT = LightsaberTag.getOrCreate(livingEntity.getUseItem());
            var vec3 = damageSource.getSourcePosition();
            if (vec3 != null) {
                var vec32 = livingEntity.getViewVector(1.0F);
                var vec33 = vec3.vectorTo(livingEntity.position()).normalize();
                vec33 = new Vec3(vec33.x, 0.0, vec33.z);
                if (vec33.dot(vec32) < 0.0) {
                    cir.setReturnValue(lT.isActive());
                }
            }
        }
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"), cancellable = true)
    public void overrideBlockSound(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (starWarsMod$LivingEntity.getMainHandItem().is(ModItems.LIGHTSABER.get())) {
            if (source.getDirectEntity() instanceof LivingEntity le && le.getMainHandItem().is(ModItems.LIGHTSABER.get()))
                starWarsMod$LivingEntity.level().playSound(null, source.getEntity().getOnPos().above(), ModSounds.LIGHTSABER_CLASH.get(), SoundSource.PLAYERS, 0.25f, 1.0f);
            else LightsaberAudio.playDeflectAudio(starWarsMod$LivingEntity.level(), starWarsMod$LivingEntity.getOnPos().above());

            var bl = ((LivingEntity) (Object) this).isDamageSourceBlocked(source) && amount > 0.0f;
            var g = 0.0f;
            if (bl) g = amount;

            boolean bl3 = !(bl) || amount > 0.0f;
            if (bl3) {
                lastDamageSource = source;
                lastDamageStamp = starWarsMod$LivingEntity.level().getGameTime();
            }

            if (starWarsMod$LivingEntity instanceof ServerPlayer sp) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(sp, source, amount, amount, bl);
                if (g > 0.0F && g < 3.4028235E37F) sp.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(g * 10.0F));
            }

            if (source.getEntity() instanceof ServerPlayer sp) CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(sp, starWarsMod$LivingEntity, source, amount, amount, bl);
            cir.setReturnValue(bl3);
        }
    }
}
