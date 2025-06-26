package mod.syconn.swm.core;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageSources {

    public static DamageSource lightsaber(Entity entity, Entity owner) {
        return new IndirectEntityDamageSource("lightsaber", entity, owner);
    }
}
