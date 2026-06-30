package mod.syconn.swm.utils.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.mixin.client.ItemRendererInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface IModifiedItemRenderer {

    Map<Class<? extends Item>, IModifiedItemRenderer> INSTANCES = new HashMap<>();

    static void register(Class<? extends Item> clazz, IModifiedItemRenderer renderer) {
        INSTANCES.put(clazz, renderer);
    }

    boolean render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model);

    default void renderItemModel(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model) {
        var invoker = ((ItemRendererInvoker) Minecraft.getInstance().getItemRenderer());
        var renderType = ItemBlockRenderTypes.getRenderType(stack, true);
        var vertexConsumer = invoker.getFoil(buffer, renderType, true, stack.hasFoil());

        poseStack.pushPose();
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        invoker.renderModel(model, stack, light, overlay, poseStack, vertexConsumer);
        poseStack.popPose();
    }

    static BakedModel getModel(ResourceLocation modelPath, BakedModel backupModel) {
        var model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(modelPath, "inventory"));
        return model == Minecraft.getInstance().getModelManager().getMissingModel() ? backupModel : model;
    }
}
