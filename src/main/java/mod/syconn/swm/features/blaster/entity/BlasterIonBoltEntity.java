package mod.syconn.swm.features.blaster.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BlasterIonBoltEntity extends BlasterBoltEntity {

    public BlasterIonBoltEntity(EntityType<? extends BlasterIonBoltEntity> type, Level level)
    {
        super(type, level);
    }

    public BlasterIonBoltEntity(EntityType<? extends BlasterIonBoltEntity> type, LivingEntity owner, Level level, boolean ignoreWater) {
        super(type, owner, level, ignoreWater);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity hitEntity = result.getEntity();
        Entity owner = this.getOwner();

        //			this.playSound(this.sound, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

        // TODO: electrical effects

        this.discard();
    }

    @Override
    protected boolean deflect(LivingEntity entity)
    {
        return false;
    }

    @Override
    protected boolean shouldDestroyBlocks()
    {
        return false;
    }
}
