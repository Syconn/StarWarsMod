package mod.syconn.swm.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (!itemStack.isEmpty()) {
            final IModifiedItemRenderer itemRenderer = IModifiedItemRenderer.INSTANCES.get(itemStack.getItem().getClass());
            if (itemRenderer != null) {
                itemRenderer.render(null, itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
//                ci.cancel();
            }
        }
    }
}
