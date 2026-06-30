package mod.syconn.swm.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageSources {

    public static DamageSource lightsaber(Level level, Entity direct, Entity source) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModTags.LIGHTSABER_DAMAGE), direct, source);
    }

    public static DamageSource blaster(Level level, Entity direct, Entity source) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModTags.BLASTER_DAMAGE), direct, source);
    }
}
