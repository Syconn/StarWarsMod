package mod.syconn.swm.core;

import mod.syconn.swm.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final ResourceKey<DamageType> LIGHTSABER_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.withId("lightsaber_damage"));
    public static final ResourceKey<DamageType> BLASTER_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.withId("blaster_damage"));

    public static final TagKey<Block> BLASTER_DEFLECTION = TagKey.create(Registries.BLOCK, Constants.withId("blaster_deflection"));
    public static final TagKey<Block> BLASTER_DESTROY = TagKey.create(Registries.BLOCK, Constants.withId("blaster_deflection"));
    public static final TagKey<Block> BLASTER_EXPLODE = TagKey.create(Registries.BLOCK, Constants.withId("blaster_deflection"));
}
