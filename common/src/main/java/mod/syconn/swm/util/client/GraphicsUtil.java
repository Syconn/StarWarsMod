package mod.syconn.swm.util.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.function.Function;

public class GraphicsUtil {

    public static void renderHSVSquare(PoseStack poseStack, int x, int y, int width, int height, Function<Integer, Integer> hsvColor) {
        for (int k = 0; k < width; k++) {
            var hsv = hsvColor.apply(k);
            var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(hsv), ColorUtil.hsvGetS(hsv), ColorUtil.hsvGetV(hsv));
            fillRect(poseStack, x + k, y, 1, height, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), 255);
        }
    }

    public static void fillRect(PoseStack poseStack, int pMinX, int pMinY, int pWidth, int pHeight, int pRed, int pGreen, int pBlue, int pAlpha) {
        int pMaxX = pMinX + pWidth;
        int pMaxY = pMinY + pHeight;
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrix4f, (float)pMinX, (float)pMinY, (float)0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMinX, (float)pMaxY, (float)0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMaxX, (float)pMaxY, (float)0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMaxX, (float)pMinY, (float)0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    public static void blitWithBorder(GuiComponent graphics, PoseStack poseStack, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

       graphics.blit(poseStack, x, y, u, v, leftBorder, topBorder);
       graphics.blit(poseStack, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder);
       graphics.blit(poseStack, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder);
       graphics.blit(poseStack, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
           graphics.blit(poseStack, x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder);
           graphics.blit(poseStack, x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder);

            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
               graphics.blit(poseStack, x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight));
        }

        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
           graphics.blit(poseStack, x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight));
           graphics.blit(poseStack, x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight));
        }
    }

    public static void renderLightsaber(PoseStack poseStack, ItemStack stack, double x, double y, float rotation) {
        final var minecraft = GameInstance.getClient();

        if (minecraft != null) {
            final var level = minecraft.level;
            final var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

            poseStack.pushPose();
            poseStack.translate(x, y, 50.0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(-90f));
            poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
            poseStack.scale(100, 100, 100);
            poseStack.scale(1.0F, -1.0F, 1.0F);

            if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
                var model = minecraft.getItemRenderer().getModel(stack, level, minecraft.player, 0);
                if (!model.usesBlockLight()) Lighting.setupForFlatItems();

                LightsaberContent.renderFixes(ItemDisplayContext.NONE, poseStack, stack);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);

                if (!model.usesBlockLight()) Lighting.setupFor3DItems();
            }
            RenderSystem.disableDepthTest();
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            poseStack.popPose();
        }
    }
}
