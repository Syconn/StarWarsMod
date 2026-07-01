package mod.syconn.swm.features.blaster.client.particle;

import mod.syconn.swm.utils.client.CrossPointingParticle;
import mod.syconn.swm.utils.client.ExpandedParticleType;
import mod.syconn.swm.utils.generic.AnimationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SparkParticle extends CrossPointingParticle {

	protected SparkParticle(ClientLevel clientLevel, double x, double y, double z, double vX, double vY, double vZ, SpriteSet spriteSet) {
		super(clientLevel, x, y, z, spriteSet);
		this.friction = 1;
		this.quadSize = (float)(this.random.nextFloat() * 0.05 + 0.06);
		this.setAlpha(1.0F);
		this.setColor(1, 0, 0);
		this.lifetime = (int)(this.random.nextFloat() * 10 + 5);
		this.setSpriteFromAge(spriteSet);
		this.hasPhysics = true;
		this.xd = vX;
		this.yd = vY;
		this.zd = vZ;
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public int getLightColor(float tint) {
		float f = ((float)this.age + tint) / (float)this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(tint);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f * 15.0F * 16.0F);
		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.removed) {
			this.setSpriteFromAge(this.sprites);
			if (this.age > this.lifetime / 2) this.setAlpha(1.0F - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);
			if (this.level.getBlockState(new BlockPos(Mth.floor(this.x), Mth.floor(this.y), Mth.floor(this.z))).isAir()) this.yd -= 0.0245;

			var a = (this.age / (float)this.lifetime);
			this.setColor(1, Mth.clamp(AnimationUtil.outCubic(3 * a), 0, 1), Mth.clamp(AnimationUtil.outCubic(1.5f * a), 0, 1));
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Provider implements ParticleProvider<ExpandedParticleType> {
		private final SpriteSet spriteProvider;

		public Provider(SpriteSet spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(ExpandedParticleType defaultParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
			return new SparkParticle(clientLevel, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
