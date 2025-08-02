package mod.syconn.swm.utils.generic;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.features.lightsaber.client.item.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.utils.client.NodeVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;

import java.util.function.Function;

public class GraphicsUtil {

    public static void blitSliced(GuiGraphics graphics, ResourceLocation location, int x, int y, int height, int uWidth, int uHeight, int uX, int uY) {
        graphics.blit(location, x, y, uX, uY, uWidth, uHeight);
        for (int i = 0; i < height; i++) graphics.blit(location, x, y + uHeight + i, uX, uY + 1, uWidth, 1);
        graphics.blit(location, x, y + uHeight + height, uX, uY + 2, uWidth, uHeight);
    }

    public static void renderHSVSquare(GuiGraphics graphics, int x, int y, int width, int height, Function<Integer, Integer> hsvColor) {
        for (int k = 0; k < width; k++) {
            var hsv = hsvColor.apply(k);
            var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(hsv), ColorUtil.hsvGetS(hsv), ColorUtil.hsvGetV(hsv));
            fillRect(graphics, x + k, y, 1, height, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 255);
        }
    }

    public static void fillRect(GuiGraphics graphics, int x, int y, int width, int height, int rgba) {
        fillRect(graphics, x, y, width, height, FastColor.ARGB32.red(rgba), FastColor.ARGB32.green(rgba), FastColor.ARGB32.blue(rgba), FastColor.ARGB32.alpha(rgba));
    }

    public static void fillRect(GuiGraphics graphics, int x, int y, int width, int height, int r, int g, int b, int a) {
        int pMaxX = x + width;
        int pMaxY = y + height;
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrix4f, (float)x, (float)y, (float)0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x, (float)pMaxY, (float)0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMaxX, (float)pMaxY, (float)0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMaxX, (float)y, (float)0).color(r, g, b, a).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    public static void blitWithBorder(GuiGraphics graphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

       graphics.blit(texture, x, y, u, v, leftBorder, topBorder);
       graphics.blit(texture, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder);
       graphics.blit(texture, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder);
       graphics.blit(texture, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
           graphics.blit(texture, x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder);
           graphics.blit(texture, x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder);

            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
               graphics.blit(texture, x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight));
        }

        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
           graphics.blit(texture, x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight));
           graphics.blit(texture, x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight));
        }
    }

    public static void renderLightsaberFromBlade(GuiGraphics guiGraphics, ItemStack stack, double x, double y, float yRot) {
        final var minecraft = GameInstance.getClient();

        if (minecraft != null) {
            final var emitterPos = LightsaberTag.getOrCreate(stack).getPrimaryBlade() != null ? LightsaberTag.getOrCreate(stack).getPrimaryBlade().emitterPos : new NodeVec3();
            final var hiltLength = LightsaberTag.getOrCreate(stack).hiltLength();
            final var upScale = 100f;
            final var scale = LightsaberItemRender.getScalar(stack).scale(upScale);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x - emitterPos.x() * scale.x(), y + (emitterPos.y() - hiltLength / 2) * scale.y(), 50.0);
            guiGraphics.pose().rotateAround(Axis.ZP.rotationDegrees(-90f), (float) (emitterPos.x() * scale.x()), (float) ((hiltLength / 2 - emitterPos.y()) * scale.y()), 0);
            guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-yRot));
            guiGraphics.pose().scale(upScale, upScale, upScale);
            guiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));

            if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, 15728880,
                    OverlayTexture.NO_OVERLAY, guiGraphics.pose(), guiGraphics.bufferSource(), minecraft.level, 0);

            guiGraphics.flush();
            guiGraphics.pose().popPose();
        }
    }

    public static void renderLightsaberFromBehind(GuiGraphics guiGraphics, ItemStack stack, double x, double y, float yRot) {
        renderLightsaberFromBehind(guiGraphics, stack, x, y, yRot, -1);
    }

    public static void renderLightsaberFromBehind(GuiGraphics guiGraphics, ItemStack stack, double x, double y, float yRot, int targetBlade) {
        final var minecraft = GameInstance.getClient();

        if (minecraft != null && stack.getItem() instanceof LightsaberItem) {
            final var lT = LightsaberTag.getOrCreate(stack);
            final var primaryEmitterPos = targetBlade == -1 && lT.getPrimaryBlade() != null || lT.getPrimaryBlade() != null ? lT.getPrimaryBlade().emitterPos : new NodeVec3();
            final var targetBladeEmitterPos = targetBlade != -1 && lT.blades.size() >= targetBlade ? lT.blades.get(targetBlade).emitterPos : new NodeVec3();
            final var hiltLength = lT.hiltLength();
            final var upScale = 100f;
            final var scale = LightsaberItemRender.getScalar(stack).scale(upScale);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + primaryEmitterPos.x() * scale.x(), y + (primaryEmitterPos.y() - hiltLength) * scale.y(), 50);
            guiGraphics.pose().rotateAround(Axis.ZN.rotationDegrees(90f), (float) (targetBladeEmitterPos.x() * scale.x()), (float) ((hiltLength - primaryEmitterPos.y()) * scale.y()), 0);
            guiGraphics.pose().rotateAround(targetBladeEmitterPos.q(), (float) (targetBladeEmitterPos.x() * scale.x()), (float) ((hiltLength / 2 - primaryEmitterPos.y()) * scale.y()), 0);
            guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-yRot));
            guiGraphics.pose().scale(upScale, upScale, upScale);
            guiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));

            if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, 15728880,
                    OverlayTexture.NO_OVERLAY, guiGraphics.pose(), guiGraphics.bufferSource(), minecraft.level, 0);

            guiGraphics.flush();
            guiGraphics.pose().popPose();
        }
    }
}
