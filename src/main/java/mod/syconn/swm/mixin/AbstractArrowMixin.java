package mod.syconn.swm.mixin;

import mod.syconn.swm.registry.ModItems;
import mod.syconn.swm.utils.client.SoundHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin { // TODO MAKE THIS ALL PROJECTILE TYPES

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    public void arrowHitEntity(EntityHitResult result, CallbackInfo ci) {
        var arrow = (AbstractArrow) (Object) this;
        var entity2 = arrow.getOwner();
        var entity = result.getEntity();
        var damageSource = arrow.damageSources().arrow(arrow, arrow);
        if (entity2 != null) {
            damageSource = arrow.damageSources().arrow(arrow, entity2);
            if (entity2 instanceof LivingEntity) ((LivingEntity) entity2).setLastHurtMob(entity);
        }

        if (result.getEntity() instanceof LivingEntity livingEntity && livingEntity.isDamageSourceBlocked(damageSource) && livingEntity.getUseItem().is(ModItems.LIGHTSABER.get())) {
            arrow.discard();
            SoundHelper.playDeflectAudio(livingEntity.level(), livingEntity.getOnPos().above());
            ci.cancel();
        }
    }
}
