package mod.syconn.swm.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.util.client.render.IItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class ItemRendererMixin {

    @Shadow
    public abstract BakedModel getModel(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity, int seed);

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, Level level, int combinedLight, int combinedOverlay, int seed, CallbackInfo ci) {
        if (!itemStack.isEmpty()) {
            BakedModel bakedModel = this.getModel(itemStack, level, entity, seed);

            final IItemRenderer itemRenderer = IItemRenderer.INSTANCES.get(itemStack.getItem().getClass());
            if (itemRenderer != null) {
                itemRenderer.render(entity, itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, bakedModel);
//                ci.cancel();
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (!itemStack.isEmpty()) {
            final IItemRenderer itemRenderer = IItemRenderer.INSTANCES.get(itemStack.getItem().getClass());
            if (itemRenderer != null) {
                itemRenderer.render(null, itemStack, displayContext, false, poseStack, buffer, combinedLight, combinedOverlay, model);
//                ci.cancel();
            }
        }
    }
}
