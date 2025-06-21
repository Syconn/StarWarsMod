package mod.syconn.swm.features.lightsaber.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.client.render.entity.PlasmaRenderer;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.client.model.ModelUtil;
import mod.syconn.swm.util.client.render.IModifiedItemRenderer;
import mod.syconn.swm.util.client.render.IModifiedPoseRenderer;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class LightsaberItemRender implements IModifiedItemRenderer, IModifiedPoseRenderer {

    @Override
    public void render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, BakedModel model) {
        poseStack.pushPose();

        model.getTransforms().getTransform(renderMode).apply(leftHanded, poseStack);
        renderDirect(stack, renderMode, poseStack, bufferSource, light, overlay);

        poseStack.popPose();
    }

    public void renderDirect(ItemStack stack, ItemDisplayContext renderMode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) { // TODO MAKE THE UNSTABLE BETTER
        if (!(stack.getItem() instanceof LightsaberItem)) return;

        var lT = LightsaberTag.getOrCreate(stack);

        if (renderMode != ItemDisplayContext.GUI) {
            for (int i = 0; i < lT.emitterPositions.size(); i++) {
                poseStack.pushPose();
                var bladePos = lT.emitterPositions.get(i);
                poseStack.translate(-bladePos.x, -bladePos.y, -bladePos.z);
                poseStack.mulPose(bladePos.q);
                if (lT.color != -1) PlasmaRenderer.renderPlasma(poseStack, bufferSource, light, overlay, !lT.stable, lT.getSize(StarWarsClient.getTickDelta()), lT.lengthScalar * bladePos.scalar,
                        (float) lT.radius, true, lT.color);
                else PlasmaRenderer.renderDarksaber(poseStack, bufferSource, light, overlay, lT.getSize(StarWarsClient.getTickDelta()), lT.lengthScalar, ColorUtil.packHsv(0f, 0f, 0.65f));
                poseStack.popPose();
            }
        }
    }

    public void modifyPose(LivingEntity entity, InteractionHand hand, ItemStack stack, HumanoidModel<? extends LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float tickDelta) {
        var mc = GameInstance.getClient();
        if (mc.player == entity && mc.options.getCameraType().isFirstPerson() && hand == InteractionHand.OFF_HAND) return;

        if (entity.isUsingItem()) {
            var delta = getBlockAnimationDelta(entity, tickDelta);
            ModelUtil.smartLerpArmsRadians(entity, hand, model, delta, -1.164f, 0.602f, 0.426f, -1.672f, -0.266f, 0.882f);
        }
    }

    private static float getBlockAnimationDelta(LivingEntity entity, float tickDelta) {
        return Mth.clamp(entity.getUseItemRemainingTicks() + tickDelta, 0, 2) / 2f;
    }
}
