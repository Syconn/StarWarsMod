package mod.syconn.swm.registry;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import mod.syconn.swm.features.blaster.client.particle.ScorchParticle;
import mod.syconn.swm.features.blaster.client.particle.SlugTrailParticle;
import mod.syconn.swm.features.blaster.client.particle.SparkParticle;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.ExpandedParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import static mod.syconn.swm.utils.Constants.MOD;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(MOD, Registries.PARTICLE_TYPE);

    public static final DeferredSupplier<ExpandedParticleType> SLUG_TRAIL = register(Constants.withId("slug_trail"), true, SlugTrailParticle.Factory::new);
    public static final DeferredSupplier<ExpandedParticleType> SPARK = register(Constants.withId("spark"), true, SparkParticle.Factory::new);
    public static final DeferredSupplier<ExpandedParticleType> SCORCH = register(Constants.withId("scorch"), true, ScorchParticle.Factory::new);

    private static DeferredSupplier<ExpandedParticleType> register(ResourceLocation location, boolean alwaysShow, ParticleProviderRegistry.DeferredParticleProvider<ExpandedParticleType> particleProvider) {
        var type = PARTICLES.register(location, () -> new ExpandedParticleType(alwaysShow));
        ParticleProviderRegistry.register(type, particleProvider);
        return type;
    }
}
