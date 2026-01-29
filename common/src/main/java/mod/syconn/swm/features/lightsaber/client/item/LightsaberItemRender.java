package mod.syconn.swm.features.lightsaber.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.utils.client.PlasmaRenderer;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.BladeData;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.mixin.client.ItemRendererInvoker;
import mod.syconn.swm.utils.generic.ModelUtil;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import mod.syconn.swm.utils.interfaces.IModifiedPoseRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import static mod.syconn.swm.features.addons.LightsaberContent.*;

public class LightsaberItemRender implements IModifiedItemRenderer, IModifiedPoseRenderer {

    @Override
    public boolean render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, BakedModel backupModel) {
        poseStack.pushPose();

        var model = IModifiedItemRenderer.getModel(LightsaberTag.getOrCreate(stack).model, backupModel);
        var transforms = model.getTransforms();

        transforms.getTransform(renderMode).apply(leftHanded, poseStack);
        if (renderMode.firstPerson() && entity != null && entity.isUsingItem() && entity.getUseItem().equals(stack)) {
            var delta = getBlockAnimationDelta(entity, StarWarsClient.getTickDelta());
            if (leftHanded) delta = -delta;
            poseStack.translate(-0.1, -0.1, 0);
            poseStack.mulPose(Axis.ZN.rotationDegrees(-50f * delta));
        } else if (renderMode == ItemDisplayContext.NONE) poseStack.scale(transforms.thirdPersonRightHand.scale.x, transforms.thirdPersonRightHand.scale.y, transforms.thirdPersonRightHand.scale.z);

        renderLightsaberBlade(stack, renderMode, poseStack, bufferSource, light, overlay);
        renderItemModel(stack, poseStack, bufferSource, light, overlay, model);

        poseStack.popPose();
        return true;
    }

    public static Vec3 getScalar(ItemStack stack) {
        var model = IModifiedItemRenderer.getModel(LightsaberTag.getOrCreate(stack).model, GameInstance.getClient().getItemRenderer().getItemModelShaper().getItemModel(stack));
        return new Vec3(model.getTransforms().getTransform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale);
    }

    private void renderLightsaberBlade(ItemStack stack, ItemDisplayContext renderMode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (!(stack.getItem() instanceof LightsaberItem)) return;

        var lT = LightsaberTag.getOrCreate(stack);
        if (renderMode != ItemDisplayContext.GUI) {
            for (int i = 0; i < lT.blades.size(); i++) {
                poseStack.pushPose();
                var blade = lT.blades.get(i);
                poseStack.mulPoseMatrix(blade.emitterPos.matrix4f());
                renderBlade(poseStack, bufferSource, light, overlay, blade);
                poseStack.popPose();
            }
        }
    }

    private void renderBlade(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, BladeData blade) {
        switch (blade.bladeType) {
            case DARK_SABER -> PlasmaRenderer.renderDarksaber(poseStack, bufferSource, light, overlay, blade.getSize(), blade.bladeLengthScalar, blade.color);
            case PLASMA -> PlasmaRenderer.renderPlasma(poseStack, bufferSource, light, overlay, !blade.stable, blade.getSize(), blade.bladeLengthScalar, (float) blade.radius, true, blade.color, false);
            case BRICK -> PlasmaRenderer.renderBrick(poseStack, bufferSource, light, overlay, blade.getSize(), blade.bladeLengthScalar, blade.color);
        }
    }

    public void modifyPose(LivingEntity entity, InteractionHand hand, ItemStack stack, HumanoidModel<? extends LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float tickDelta) {
        var mc = GameInstance.getClient();
        if (mc.player == entity && mc.options.getCameraType().isFirstPerson() && hand == InteractionHand.OFF_HAND) return;

        if (entity.isUsingItem()) {
            var delta = this.getBlockAnimationDelta(entity, tickDelta);
            ModelUtil.smartLerpArmsRadians(entity, hand, model, delta, -1.164f, 0.602f, 0.426f, -1.672f, -0.266f, 0.882f);
        }
    }

    private float getBlockAnimationDelta(LivingEntity entity, float tickDelta) {
        return Mth.clamp(entity.getUseItemRemainingTicks() + tickDelta, 0, 2) / 2f;
    }
}
