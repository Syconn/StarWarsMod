package mod.syconn.swm.features.lightsaber.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaber;
import mod.syconn.swm.util.math.MathUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ThrownLightsaberRenderer extends EntityRenderer<ThrownLightsaber> {

    private final ItemRenderer itemRenderer;

    public ThrownLightsaberRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public @NotNull ResourceLocation getTextureLocation(ThrownLightsaber entity) {
        return new ResourceLocation("missing");
    }

    public void render(ThrownLightsaber entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5f * entity.getEyeHeight(), 0);

        var velocity = entity.getDeltaMovement();
        velocity = velocity.normalize();
        var bYaw = (float) Math.atan2(velocity.x, velocity.z);
        var bPitch = (float) Math.asin(velocity.y);

        poseStack.mulPose(Vector3f.YP.rotation(bYaw));
        poseStack.mulPose(Vector3f.XP.rotation((float)(Math.PI / 2) - bPitch));
        poseStack.mulPose(Vector3f.ZP.rotation(MathUtil.toRadians(-(entity.tickCount + partialTick) * 31)));

        this.itemRenderer.renderStatic(entity.getItem(), ItemTransforms.TransformType.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getId());

        poseStack.popPose();
    }
}
