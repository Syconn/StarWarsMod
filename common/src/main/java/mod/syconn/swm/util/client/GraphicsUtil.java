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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.function.Function;

public class GraphicsUtil {

    public static void renderHSVSquare(GuiGraphics graphics, int x, int y, int width, int height, Function<Integer, Integer> hsvColor) {
        for (int k = 0; k < width; k++) {
            var hsv = hsvColor.apply(k);
            var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(hsv), ColorUtil.hsvGetS(hsv), ColorUtil.hsvGetV(hsv));
            graphics.fill(x + k, y, x + k + 1, y + height, FastColor.ARGB32.color(255, color));
        }
    }

    public static void renderLightsaber(GuiGraphics guiGraphics, ItemStack stack, double x, double y, float rotation) {
        final var minecraft = GameInstance.getClient();

        if (minecraft != null) {
            final var level = minecraft.level;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 50.0);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-90f));
            guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-rotation));
            guiGraphics.pose().scale(100, 100, 100);
            guiGraphics.pose().mulPose(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));

            if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
                var model = minecraft.getItemRenderer().getModel(stack, level, minecraft.player, 0);
                if (!model.usesBlockLight()) Lighting.setupForFlatItems();

                LightsaberContent.renderFixes(ItemDisplayContext.NONE, guiGraphics.pose(), stack);
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, model);

                if (!model.usesBlockLight()) Lighting.setupFor3DItems();
            }
            guiGraphics.flush();
            guiGraphics.pose().popPose();
        }
    }
}
