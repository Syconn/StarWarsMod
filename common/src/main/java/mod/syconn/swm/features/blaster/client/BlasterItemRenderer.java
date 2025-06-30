package mod.syconn.swm.features.blaster.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.utils.client.model.ModelUtil;
import mod.syconn.swm.utils.client.render.IModifiedItemRenderer;
import mod.syconn.swm.utils.client.render.IModifiedPoseRenderer;
import mod.syconn.swm.utils.math.MathUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BlasterItemRenderer implements IModifiedItemRenderer, IModifiedPoseRenderer {

    public void render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model) {
        poseStack.pushPose();

        model.getTransforms().getTransform(renderMode).apply(leftHanded, poseStack);
//        renderDirect(stack, renderMode, poseStack, bufferSource, light, overlay);

        poseStack.popPose();
    }

    public void modifyPose(LivingEntity entity, InteractionHand hand, ItemStack stack, HumanoidModel<? extends LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float tickDelta) {
        var mc = GameInstance.getClient();
        if (mc.player == entity && mc.options.getCameraType().isFirstPerson() && hand == InteractionHand.OFF_HAND) return;

        var delta = getBlockAnimationDelta(entity, tickDelta);
        var head = model.head;
        var left = (entity.getMainArm() == HumanoidArm.LEFT) ^ (hand == InteractionHand.OFF_HAND);
        if (entity.isUsingItem()) ModelUtil.smartLerpArmsRadians(entity, hand, model, delta, -1.164f + headPitch * (float) (Math.PI / 180.0), 0.602f, 0.426f,
                MathUtil.toRadians(-145) + headPitch * (float) (Math.PI / 180.0), -0.266f, 0);
        else ModelUtil.smartLerpArmsRadians(entity, hand, model, 1, 0, 0, 0,
                MathUtil.toRadians(-145) + headPitch * (float) (Math.PI / 180.0), head.yRot * (left ? -1 : 1), 0);
    }

    private static float getBlockAnimationDelta(LivingEntity entity, float tickDelta) {
        return Mth.clamp(entity.getUseItemRemainingTicks() + tickDelta, 0, 2) / 2f;
    }
}
