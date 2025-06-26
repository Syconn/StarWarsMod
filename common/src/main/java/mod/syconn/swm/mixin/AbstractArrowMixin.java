package mod.syconn.swm.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    public void arrowHitEntity(EntityHitResult result, CallbackInfo ci) {
        var arrow = (AbstractArrow) (Object) this;
        var entity2 = arrow.getOwner();
        var entity = result.getEntity();
        var damageSource = DamageSource.arrow(arrow, arrow);
        if (entity2 != null) {
            damageSource = DamageSource.arrow(arrow, entity2);
            if (entity2 instanceof LivingEntity) ((LivingEntity) entity2).setLastHurtMob(entity);
        }

        if (result.getEntity() instanceof LivingEntity livingEntity && livingEntity.isDamageSourceBlocked(damageSource)) {
            arrow.discard();
            ci.cancel();
        }
    }
}
