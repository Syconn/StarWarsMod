package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.utils.client.ExpandedParticleType;

public class ModParticles {

    public static final RegistryEntry<ExpandedParticleType> SLUG_TRAIL = RegistryEntry.particleType("slug_trail", () -> new ExpandedParticleType(true));
    public static final RegistryEntry<ExpandedParticleType> SPARK = RegistryEntry.particleType("spark", () -> new ExpandedParticleType(true));
    public static final RegistryEntry<ExpandedParticleType> SCORCH = RegistryEntry.particleType("scorch", () -> new ExpandedParticleType(true));
}
