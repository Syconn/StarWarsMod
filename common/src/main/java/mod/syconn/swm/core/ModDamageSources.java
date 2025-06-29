package mod.syconn.swm.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class ModDamageSources {

    public static DamageSource lightsaber(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModTags.LIGHTSABER_DAMAGE));
    }

    public static DamageSource blaster(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModTags.LIGHTSABER_DAMAGE));
    }
}
