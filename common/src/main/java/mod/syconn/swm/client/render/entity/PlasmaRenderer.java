package mod.syconn.swm.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.client.PlasmaBuffer;
import mod.syconn.swm.util.math.ColorUtil;
import mod.syconn.swm.util.math.MathUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import static mod.syconn.swm.util.math.ColorUtil.*;

public class PlasmaRenderer {

    public static final RenderType PLASMA = RenderType.create(Constants.MOD + ":plasma", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder().setLayeringState(RenderStateShard.LayeringStateShard.VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setShaderState(RenderStateShard.RENDERTYPE_LIGHTNING_SHADER).createCompositeState(true));
    private static final RenderType PLASMA_ADDITIVE = RenderType.create(Constants.MOD + "energy_add", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder().setLayeringState(RenderStateShard.LayeringStateShard.VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
            .setShaderState(RenderStateShard.RENDERTYPE_LIGHTNING_SHADER).createCompositeState(true));

    public static void renderPlasma(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, boolean unstable, float length, float lengthScalar, float radius, boolean cap, int glowHsv) {
        final VertexConsumer vc = bufferSource.getBuffer(PLASMA);

        var totalLength = length * lengthScalar * 1.3f;
        var shake = (0.2f) * 0.004f;

        if (unstable) shake *= 2;

        double dX = (float)Constants.RANDOM.nextGaussian() * shake;
        double dY = (float)Constants.RANDOM.nextGaussian() * shake;
        poseStack.translate(dX, 0, dY);

        PlasmaBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, 1, overlay, light);
        renderGlow(totalLength, radius, ColorUtil.hsvGetH(glowHsv), ColorUtil.hsvGetS(glowHsv), ColorUtil.hsvGetV(glowHsv), unstable, cap);
    }


    public static void renderBrick(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, float length, float lengthScalar, int glowHsv) {
        final VertexConsumer vc = bufferSource.getBuffer(PLASMA);

        var totalLength = length * lengthScalar;
        var shake = (1.1f - lengthScalar) * 0.004f;

        double dX = (float) Constants.RANDOM.nextGaussian() * shake;
        double dY = (float) Constants.RANDOM.nextGaussian() * shake;
        poseStack.translate(dX, 0, dY);

        PlasmaBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, 1, overlay, light);

        var thickness = 0.036f;

        var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(glowHsv), ColorUtil.hsvGetS(glowHsv), ColorUtil.hsvGetV(glowHsv));
        PlasmaBuffer.RENDER.setColor(color, 128);

        PlasmaBuffer.RENDER.invertCull(true);
        PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thickness, thickness, 0, totalLength, 0, 0, 0, 0);
        PlasmaBuffer.RENDER.invertCull(false);

        PlasmaBuffer.RENDER.invertCull(true);
        PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thickness * 0.6f, thickness * 0.6f, 0, 0.3f * totalLength, 0, 0, 0.35f * totalLength, 0);
        PlasmaBuffer.RENDER.invertCull(false);
    }

    public static void renderDarksaber(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, float length, float lengthScalar, int glowHsv) {
        final VertexConsumer vc = bufferSource.getBuffer(PLASMA);

        var totalLength = length * lengthScalar;
        var shake = (1.1f - lengthScalar) * 0.004f;

        double dX = (float) Constants.RANDOM.nextGaussian() * shake;
        double dY = (float) Constants.RANDOM.nextGaussian() * shake;
        poseStack.translate(dX, 0, dY);
        poseStack.scale(3, 3, 3);

        PlasmaBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, 1, overlay, light);
        renderDarkSaberGlow(totalLength, ColorUtil.hsvGetH(glowHsv), ColorUtil.hsvGetS(glowHsv), ColorUtil.hsvGetV(glowHsv));
    }

    public static void renderStunEnergy(ItemDisplayContext renderMode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, float size, Vec3 normal, float glowHue) {
        final VertexConsumer vc = bufferSource.getBuffer(PLASMA_ADDITIVE);

        PlasmaBuffer.RENDER.init(vc, poseStack.last(), 0.1f, 0.2f, 1, 1.0f, overlay, light);

        size /= 2;
        var nx = 0f;
        var ny = 0f;
        var nz = 1f;
        var d = 0.8f * size;

        // front cull
        PlasmaBuffer.RENDER.vertex(-size, size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(size, size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-d, d, 0, nx, ny, nz, 0, 0);

        PlasmaBuffer.RENDER.vertex(size, size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(size, -size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, d, 0, nx, ny, nz, 0, 0);

        PlasmaBuffer.RENDER.vertex(-size, -size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(size, -size, 0, nx, ny, nz, 0, 0);

        PlasmaBuffer.RENDER.vertex(-size, -size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-size, size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-d, d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-d, -d, 0, nx, ny, nz, 0, 0);

        // back cull
        PlasmaBuffer.RENDER.vertex(-d, d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(size, size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-size, size, 0, nx, ny, nz, 0, 0);

        PlasmaBuffer.RENDER.vertex(d, d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(size, -size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(size, size, 0, nx, ny, nz, 0, 0);

        PlasmaBuffer.RENDER.vertex(size, -size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-size, -size, 0, nx, ny, nz, 0, 0);

        PlasmaBuffer.RENDER.vertex(-d, -d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-d, d, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-size, size, 0, nx, ny, nz, 0, 0);
        PlasmaBuffer.RENDER.vertex(-size, -size, 0, nx, ny, nz, 0, 0);
    }

    private static void renderGlow(float length, float radius, float glowHue, float glowSat, float glowVal, boolean unstable, boolean cap) {
        if (length == 0) return;

        var thicknessBottom = radius * 0.018f;
        var thicknessTop = radius * (cap ? 0.012f : thicknessBottom);

        var mL = 0;
        var xL = 14;

        var deltaThickness = radius * 0.0028f;
        var minOutputLayer = mL * thicknessBottom / deltaThickness;
        var globalTime = StarWarsClient.getTickDelta();

        for (var layer = mL; layer <= xL; layer++) {
            var time = ((System.currentTimeMillis() - layer * 10) % Integer.MAX_VALUE) / 200f;
            var noise = (float) Constants.SIMPLEX.getValue(0, time);

            var hueOffset = unstable ? (noise * 0.025f) : 0;

            var x = MathUtil.remap(layer, mL, xL, minOutputLayer, 60);
            var alpha = getAlpha(x);
            if (alpha < 16 / 255f) continue;

            var color = ColorUtil.hsvToRgbInt(getHue(glowHue + hueOffset, x), getSaturation(x, glowSat), getValue(x, glowVal));
            PlasmaBuffer.RENDER.setColor(color, (int)(255 * alpha));
            var layerThickness = deltaThickness * layer;

            if (layer > 0) {
                PlasmaBuffer.RENDER.invertCull(true);
                PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thicknessTop + layerThickness, thicknessBottom + layerThickness, 0, length + layerThickness, 0, 0, -layerThickness, 0);
                PlasmaBuffer.RENDER.invertCull(false);
            } else {
                final var segments = unstable ? 35 : 1;
                final var dSegments = 1f / segments;
                final var dLength = length / segments;

                final var dLengthTime = 5;

                for (var i = 0; i < segments; i++) {
                    var topThicknessLerp = Mth.lerp(dSegments * (i + 1), thicknessBottom, thicknessTop);
                    var bottomThicknessLerp = Mth.lerp(dSegments * i, thicknessBottom, thicknessTop);

                    var dTTop = unstable ? (float) Constants.SIMPLEX.getValue(globalTime, dLengthTime * dLength * (i + 1)) * 0.0075f : 0;
                    var dTBottom = unstable ? (float)Constants.SIMPLEX.getValue(globalTime, dLengthTime * dLength * i) * 0.0075f : 0;

                    noise = (float) Constants.SIMPLEX.getValue(globalTime, 3 * dLength * i);
                    color = ColorUtil.hsvToRgbInt(0, (unstable ? (0.07f - noise * 0.07f) : 0) * glowSat, getValue(x, glowVal) - 0.12f);
                    PlasmaBuffer.RENDER.setColor(color, (int)(255 * getAlpha(x)));
                    PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(topThicknessLerp + dTTop, bottomThicknessLerp + dTBottom, 0, dLength * (i + 1), 0, 0, dLength * i, 0);
                }
            }
        }
    }

    private static void renderDarkSaberGlow(float bladeLength, float glowHue, float glowSat, float glowVal) {
        if (bladeLength == 0) return;

        var thicknessBottom = 0.02f;
        var thicknessTop = 0.018f;
        var mL = 0;
        var xL = 14;
        var deltaThickness = 0.0028f;
        var minOutputLayer = mL * thicknessBottom / deltaThickness;

        PlasmaBuffer.RENDER.setScale(0.5f, 1, 1);

        for (var layer = mL; layer <= xL; layer++) {
            var x = MathUtil.remap(layer, mL, xL, minOutputLayer, 70);
            var alpha = ColorUtil.getAlpha(x);
            if (alpha < 16 / 255f) continue;

            var layerThickness = deltaThickness * layer;

            if (layer > 0) {
                var color = ColorUtil.hsvToRgbInt(getHue(glowHue, x), getSaturation(x, glowSat), getValue(x, glowVal));
                PlasmaBuffer.RENDER.setColor(color, (int)(255 * alpha));

                PlasmaBuffer.RENDER.invertCull(true);
                PlasmaBuffer.RENDER.skipFace(1);
                PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thicknessTop + layerThickness, thicknessBottom + layerThickness, 0, bladeLength * 0.6f + layerThickness, 0, 0, -layerThickness, 0);
                PlasmaBuffer.RENDER.skipFace(3);
                PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thicknessTop / 4f + layerThickness, thicknessTop + layerThickness, 0, bladeLength + layerThickness, thicknessTop * 0.75f, 0, bladeLength * 0.6f + layerThickness, 0);
                PlasmaBuffer.RENDER.skipFace(-1);
                PlasmaBuffer.RENDER.invertCull(false);
            } else {
                PlasmaBuffer.RENDER.setColor(0x101010, (int)(255 * getAlpha(x)));
                PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thicknessTop, thicknessBottom, 0, bladeLength * 0.6f, 0, 0, 0, 0);
                PlasmaBuffer.RENDER.drawSolidBoxSkewTaper(thicknessTop / 4f, thicknessTop, 0, bladeLength, thicknessTop * 0.75f, 0, bladeLength * 0.6f, 0);
            }
        }

        PlasmaBuffer.RENDER.resetScale();
    }

    public static void renderLayer(RenderBuffers buffers) {
        buffers.outlineBufferSource().getBuffer(PLASMA).endVertex();
    }
}
