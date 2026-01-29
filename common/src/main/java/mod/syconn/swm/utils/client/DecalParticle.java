package mod.syconn.swm.utils.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kosmx.playerAnim.core.util.MathHelper;
import mod.syconn.swm.utils.generic.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class DecalParticle extends SimpleAnimatedParticle {

    protected DecalParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteProvider) {
        super(level, x, y, z, spriteProvider, 0.0F);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) this.remove();

        this.setSpriteFromAge(this.sprites);
        if (this.age > this.lifetime / 2) this.setAlpha(1.0F - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);

        var normal = new Vec3(xd, yd, zd).normalize();
        var pos = new Vec3(this.x, this.y, this.z);

        var hostBlockPos = new BlockPos(MathUtil.floorInt(pos.subtract(normal.scale(0.1f))));
        if (this.level.getBlockState(hostBlockPos).isAir()) this.remove();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        var vec3 = renderInfo.getPosition();
        var f = (float)(MathHelper.lerp(partialTicks, this.xo, this.x) - vec3.x);
        var g = (float)(MathHelper.lerp(partialTicks, this.yo, this.y) - vec3.y);
        var h = (float)(MathHelper.lerp(partialTicks, this.zo, this.z) - vec3.z);

        var normal = new Vec3(xd, yd, zd).normalize();

        Quaternionf rotation = MathUtil.lookAt(Vec3.ZERO, normal);
        rotation.rotateZ(roll);

        var z = (1 - this.age / (float)this.lifetime) * 0.005f;

        var corners = new Vector3f[] {
                new Vector3f(-1.0F, -1.0F, z),
                new Vector3f(-1.0F, 1.0F, z),
                new Vector3f(1.0F, 1.0F, z),
                new Vector3f(1.0F, -1.0F, z)
        };

        var j = this.getQuadSize(partialTicks);
        var l = this.getU0();
        var m = this.getU1();
        var n = this.getV0();
        var o = this.getV1();
        var p = this.getLightColor(partialTicks);

        for (var k = 0; k < 4; ++k) {
            var vec3f2 = corners[k];
            vec3f2.rotate(rotation);
            vec3f2.mul(j);
            vec3f2.add(f, g, h);
        }

        buffer.vertex(corners[3].x, corners[3].y, corners[3].z).uv(m, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        buffer.vertex(corners[2].x, corners[2].y, corners[2].z).uv(m, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        buffer.vertex(corners[1].x, corners[1].y, corners[1].z).uv(l, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        buffer.vertex(corners[0].x, corners[0].y, corners[0].z).uv(l, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
    }
}
