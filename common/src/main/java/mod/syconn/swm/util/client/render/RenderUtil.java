package mod.syconn.swm.util.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.swm.util.client.IItemExtensions;
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
}
