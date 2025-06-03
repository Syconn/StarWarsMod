package mod.syconn.swm.registry;

import mod.syconn.swm.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModTags {

    public static final ResourceKey<DamageType> LIGHTSABER_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.withId("lightsaber_damage"));
}
