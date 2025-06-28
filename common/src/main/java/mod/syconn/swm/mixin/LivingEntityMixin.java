package mod.syconn.swm.mixin;

import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "isDamageSourceBlocked", at = @At(value = "HEAD"), cancellable = true)
    public void isDamageSourceBlocked(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        var livingEntity = (LivingEntity) (Object) this;

        if (!damageSource.is(DamageTypeTags.BYPASSES_SHIELD) && livingEntity.getUseItem().getItem() instanceof LightsaberItem) {
            var lT = LightsaberComponent.getOrCreate(livingEntity.getUseItem());
            Vec3 vec3 = damageSource.getSourcePosition();
            if (vec3 != null) {
                Vec3 vec32 = livingEntity.getViewVector(1.0F);
                Vec3 vec33 = vec3.vectorTo(livingEntity.position()).normalize();
                vec33 = new Vec3(vec33.x, 0.0, vec33.z);
                if (vec33.dot(vec32) < 0.0) {
                    cir.setReturnValue(lT.active());
                }
            }
        }
    }
}
