package mod.syconn.swm.api.registry.client;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

@FunctionalInterface
public interface ParticleProviderRegister {
    <T extends ParticleOptions> void apply(ParticleType<T> type, SpriteProvider<T> provider);

    @FunctionalInterface
    interface SpriteProvider<T extends ParticleOptions> {
        ParticleProvider<T> apply(SpriteSet sprites);
    }
}
