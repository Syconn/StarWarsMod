package mod.syconn.swm.features.blaster.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlasterStunBoltEntity extends BlasterBoltEntity {
    public BlasterStunBoltEntity(EntityType<? extends BlasterStunBoltEntity> type, Level level) {
        super(type, level);
    }

    public BlasterStunBoltEntity(EntityType<? extends BlasterStunBoltEntity> type, LivingEntity owner, Level level, boolean ignoreWater) {
        super(type, owner, level, ignoreWater);
    }

    @Override
    protected boolean shouldCreateScorch() {
        return false;
    }

    @Override
    protected boolean shouldDestroyBlocks() {
        return false;
    }

    @Override
    protected boolean deflect(LivingEntity entity) {
        return false;
    }

    @Override
    protected boolean deflect(BlockHitResult hit, BlockState state) {
        return false;
    }

    @Override
    protected void damage(Entity target) { }
}
