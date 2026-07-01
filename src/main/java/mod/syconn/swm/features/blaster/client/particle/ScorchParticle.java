package mod.syconn.swm.features.blaster.client.particle;

import dev.kosmx.playerAnim.core.util.MathHelper;
import mod.syconn.swm.utils.client.DecalParticle;
import mod.syconn.swm.utils.client.ExpandedParticleType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ScorchParticle extends DecalParticle {
	private static final int NUM_VARIANTS = 3;
	private final int variant;
	private final float heat;

	protected ScorchParticle(ClientLevel clientWorld, double x, double y, double z, double vX, double vY, double vZ, float heat, SpriteSet spriteProvider) {
		super(clientWorld, x, y, z, spriteProvider);
		this.friction = 1;
		this.quadSize = 0.1f;
		this.setAlpha(1.0F);
		var a = MathHelper.lerp(heat, 1, Mth.clamp((this.age / (float)this.lifetime) * 2f, 0, 1));
		this.setColor(Mth.clamp(getRed(a), 0, 1), Mth.clamp(getGreen(a), 0, 1), Mth.clamp(getBlue(a), 0, 1));
		this.lifetime = 200;
		this.setSpriteFromAge(spriteProvider);
		this.hasPhysics = false;
		this.xd = vX;
		this.yd = vY;
		this.zd = vZ;
		this.roll = this.random.nextFloat() * Mth.PI;
		this.heat = heat;
		this.variant = this.random.nextInt(NUM_VARIANTS);
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

	private float getRed(float t) {
		t *= 5;
		return -0.0581f * t * t + 0.0982f * t + 0.963f;
	}

	private float getGreen(float t) {
		t *= 5;
		return 0.0292f * t * t - 0.314f * t + 0.837f;
	}

	private float getBlue(float t) {
		t *= 5;
		return 0.0166f * t * t - 0.152f * t + 0.348f;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.removed) {
			var halfAge = this.lifetime / 2f;
			if (this.age > halfAge) this.setAlpha(1 - (this.age - halfAge) / halfAge);

			this.setSprite(sprites.get(this.variant, NUM_VARIANTS));
			var a = MathHelper.lerp(heat, 1, Mth.clamp((this.age / (float)this.lifetime) * 2f, 0, 1));
			this.setColor(Mth.clamp(getRed(a), 0, 1), Mth.clamp(getGreen(a), 0, 1), Mth.clamp(getBlue(a), 0, 1));
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
		public Particle createParticle(ExpandedParticleType defaultParticleType, ClientLevel clientWorld, double x, double y, double z, double vX, double vY, double vZ) {
			var heatEncodedNormal = new Vec3(vX, vY, vZ);
			var heat = heatEncodedNormal.length();
			var normal = heatEncodedNormal.normalize();
			return new ScorchParticle(clientWorld, x, y, z, normal.x, normal.y, normal.z, (float)heat, this.spriteProvider);
		}
	}
}
