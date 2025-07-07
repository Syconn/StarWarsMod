package mod.syconn.swm.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.utils.client.HologramData;
import mod.syconn.swm.utils.general.MathUtil;
import mod.syconn.swm.utils.general.ModelUtil;
import mod.syconn.swm.utils.general.RenderUtil;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import mod.syconn.swm.utils.interfaces.IModifiedPoseRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoloProjectorItemRenderer implements IModifiedItemRenderer, IModifiedPoseRenderer {

    private final Map<UUID, HologramData> RENDERER = new HashMap<>();

    @Override
    public void render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, BakedModel model) {
        poseStack.pushPose();

        model.getTransforms().getTransform(renderMode).apply(leftHanded, poseStack);
        if (renderMode != ItemDisplayContext.GUI)
            renderDirect(stack, renderMode, poseStack, bufferSource, light, overlay);

        poseStack.popPose();
    }


    private void renderDirect(ItemStack stack, ItemDisplayContext renderMode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        var uuid = HologramData.HologramTag.getOrCreate(stack).uuid;
        if (uuid != null) {
            poseStack.pushPose();

            poseStack.translate(0f, -0.43f, 0f);
            poseStack.mulPose(Axis.YN.rotationDegrees(RenderUtil.isLeftHanded(renderMode) ? -45f : 45f));
            poseStack.scale(0.6f, 0.6f, 0.6f);
            var hologramData = getOrRegisterRenderer(uuid, HologramData.HologramTag.refreshed(stack));
            hologramData.getRenderer().render(poseStack, bufferSource, StarWarsClient.getTickDelta(), LightTexture.FULL_BLOCK);

            poseStack.popPose();
        }
    }

    private HologramData getOrRegisterRenderer(UUID uuid, boolean refresh) {
        if (!RENDERER.containsKey(uuid) || refresh) RENDERER.put(uuid, new HologramData(uuid));
        return RENDERER.get(uuid);
    }

    @Override
    public void modifyPose(LivingEntity entity, InteractionHand hand, ItemStack stack, HumanoidModel<? extends LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float tickDelta) {
        var mc = GameInstance.getClient();
        if (mc.player == entity && mc.options.getCameraType().isFirstPerson()) return;

        if (HologramData.HologramTag.getOrCreate(stack).uuid != null)
            ModelUtil.smartLerpArmsRadians(entity, hand, model, 1, 0, 0, 0, MathUtil.toRadians(-145), 0, 0);
    }
}
