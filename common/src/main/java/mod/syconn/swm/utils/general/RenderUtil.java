package mod.syconn.swm.utils.general;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.swm.utils.interfaces.IItemExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RenderUtil {

    private static int slotMainHand = 0;

    public static boolean handleReequipAnimation(@NotNull ItemStack from, @NotNull ItemStack to, int slot) {
        if (from.getItem() instanceof IItemExtensions ext) {
            boolean fromInvalid = from.isEmpty();
            boolean toInvalid = to.isEmpty();

            if (fromInvalid && toInvalid) return false;
            if (fromInvalid || toInvalid) return true;

            boolean changed = false;
            if (slot != -1) {
                changed = slot != slotMainHand;
                slotMainHand = slot;
            }
            return ext.shouldCauseReequipAnimation(from, to, changed);
        }
        return true;
    }

    public static void renderModelLists(BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        RandomSource randomSource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            renderQuadList(poseStack, buffer, model.getQuads(null, direction, randomSource), stack, combinedLight, combinedOverlay);
        }

        randomSource.setSeed(42L);
        renderQuadList(poseStack, buffer, model.getQuads(null, null, randomSource), stack, combinedLight, combinedOverlay);
    }

    private static void renderQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, ItemStack itemStack, int combinedLight, int combinedOverlay) {
        boolean bl = !itemStack.isEmpty();
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad bakedQuad : quads) {
            int i = -1;
            if (bl && bakedQuad.isTinted()) {
                i = Minecraft.getInstance().getItemRenderer().itemColors.getColor(itemStack, bakedQuad.getTintIndex());
            }

            float f = (i >> 16 & 0xFF) / 255.0F;
            float g = (i >> 8 & 0xFF) / 255.0F;
            float h = (i & 0xFF) / 255.0F;
            buffer.putBulkData(pose, bakedQuad, f, g, h, combinedLight, combinedOverlay);
        }
    }
}
