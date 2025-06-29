package mod.syconn.swm.core;

import mod.syconn.swm.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModTags {

    public static final ResourceKey<DamageType> LIGHTSABER_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.withId("lightsaber_damage"));
    public static final ResourceKey<DamageType> BLASTER_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.withId("blaster_damage"));
}
