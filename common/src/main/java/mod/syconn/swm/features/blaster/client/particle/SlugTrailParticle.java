package mod.syconn.swm.features.blaster.client.particle;

import mod.syconn.swm.utils.client.ExpandedParticleType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;

@Environment(EnvType.CLIENT)
public class SlugTrailParticle extends SimpleAnimatedParticle {

	protected SlugTrailParticle(ClientLevel clientLevel, double x, double y, double z, double vX, double vY, double vZ, SpriteSet spriteSet) {
		super(clientLevel, x, y, z, spriteSet, 0.0F);
		this.friction = 0.92F;
		this.quadSize = this.random.nextFloat() * 0.2f + 0.2f;
		this.setAlpha(1.0F);
		var gray = 0.6f;
		this.setColor(gray, gray, gray);
		this.lifetime = (int)((double)(this.quadSize * 12.0F) / (this.random.nextFloat() * 0.8 + 0.2));
		this.setSpriteFromAge(spriteSet);
		this.hasPhysics = true;
		this.xd = vX;
		this.yd = vY;
		this.zd = vZ;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.removed) {
			this.setAlpha(1);
			this.setSpriteFromAge(this.sprites);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<ExpandedParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(ExpandedParticleType defaultParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
			return new SlugTrailParticle(clientLevel, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
