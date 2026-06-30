package mod.syconn.swm.utils.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.swm.utils.generic.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class CrossPointingParticle extends SimpleAnimatedParticle {
    protected CrossPointingParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet, 0.0F);
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        var vec3d = camera.getPosition();
        var f = (float)(Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x);
        var g = (float)(Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y);
        var h = (float)(Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z);

        Quaternionf rotation = MathUtil.lookAt(Vec3.ZERO, new Vec3(xd, yd, zd));
        rotation.mul(MathUtil.ROT_Y_POS90);
        rotation.mul(MathUtil.ROT_X_POS45);

        var corners = new Vector3f[] {
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        var j = this.getQuadSize(tickDelta);
        var l = this.getU0();
        var m = this.getU1();
        var n = this.getV0();
        var o = this.getV1();
        var p = this.getLightColor(tickDelta);

        for (var k = 0; k < 4; ++k) {
            var vec3f2 = corners[k];
            vec3f2.rotate(rotation);
            vec3f2.mul(j);
            vec3f2.add(f, g, h);
        }

        vertexConsumer.vertex(corners[0].x, corners[0].y, corners[0].z).uv(m, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[1].x, corners[1].y, corners[1].z).uv(m, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[2].x, corners[2].y, corners[2].z).uv(l, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[3].x, corners[3].y, corners[3].z).uv(l, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();

        vertexConsumer.vertex(corners[3].x, corners[3].y, corners[3].z).uv(l, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[2].x, corners[2].y, corners[2].z).uv(l, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[1].x, corners[1].y, corners[1].z).uv(m, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[0].x, corners[0].y, corners[0].z).uv(m, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();

        corners = new Vector3f[] {
                new Vector3f(-1.0F, 0.0F, 1.0F),
                new Vector3f(-1.0F, 0.0F, -1.0F),
                new Vector3f(1.0F, 0.0F, -1.0F),
                new Vector3f(1.0F, 0.0F, 1.0F)
        };

        for (var k = 0; k < 4; ++k) {
            var vec3f2 = corners[k];
            vec3f2.rotate(rotation);
            vec3f2.mul(j);
            vec3f2.add(f, g, h);
        }

        vertexConsumer.vertex(corners[0].x, corners[0].y, corners[0].z).uv(m, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[1].x, corners[1].y, corners[1].z).uv(m, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[2].x, corners[2].y, corners[2].z).uv(l, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[3].x, corners[3].y, corners[3].z).uv(l, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();

        vertexConsumer.vertex(corners[3].x, corners[3].y, corners[3].z).uv(l, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[2].x, corners[2].y, corners[2].z).uv(l, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[1].x, corners[1].y, corners[1].z).uv(m, n).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
        vertexConsumer.vertex(corners[0].x, corners[0].y, corners[0].z).uv(m, o).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p).endVertex();
    }
}
