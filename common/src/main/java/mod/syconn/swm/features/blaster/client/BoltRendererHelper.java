package mod.syconn.swm.features.blaster.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.render.entity.PlasmaRenderer;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.blaster.data.BlasterTag;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.util.client.PlasmaBuffer;
import mod.syconn.swm.util.math.Ease;
import mod.syconn.swm.util.math.MathUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class BoltRendererHelper {

    public static void renderStun(BlasterBoltEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, int light, float partialTicks) {
        var velocity = entity.getDeltaMovement();
        velocity = velocity.normalize();

        poseStack.pushPose();

        poseStack.translate(0, 0.5f * entity.getBbHeight(), 0);

        var rPitch = (float)Math.asin(-velocity.y);
        var rYaw = (float)Math.atan2(velocity.x, velocity.z);

        poseStack.mulPose(new Quaternionf().rotationY(rYaw));
        poseStack.mulPose(new Quaternionf().rotationX(rPitch));

        var age = entity.tickCount + partialTicks;
        var size = age / 10f;
        if (size < 3 / 16f) size = 3 / 16f;
        size = (float)Math.pow(size, 0.75f);

        PlasmaRenderer.renderStunEnergy(poseStack, bufferSource, light, 0xFFFFFF, size); // velocity, 0.6f

        poseStack.popPose();
    }

    public static void renderBolt(BlasterBoltEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int light) {
        var velocity = entity.getDeltaMovement();
        velocity = velocity.normalize();

        var bYaw = (float)Math.atan2(velocity.x, velocity.z);
        var bPitch = (float)Math.asin(velocity.y);

        if (entity.tickCount == 0) return;

        poseStack.pushPose();

        poseStack.translate(0, 0.5f * entity.getBbHeight(), 0);

        var mc = GameInstance.getClient();
        var isOwnedByClient = entity.getOwner() == mc.player;
        var isFirstPerson = mc.options.getCameraType() == CameraType.FIRST_PERSON;
        var isCameraPlayer = isFirstPerson && mc.getCameraEntity() == mc.player;
        var shouldScale = isOwnedByClient && isFirstPerson && isCameraPlayer;
        var shouldOffset = shouldScale;

        if (shouldScale) {
            var mainStack = mc.player.getMainHandItem();
            if (mainStack.getItem() instanceof BlasterItem) {
//                var bt = new BlasterTag(mainStack.getOrCreateTag());
//                shouldOffset = !bt.isAimingDownSights;
                shouldOffset = true;
            }
        }

        var ownerDist = 1d;
        if (shouldScale) {
            var dist = mc.player.getViewVector(partialTicks).distanceTo(entity.getPosition(partialTicks));
            ownerDist = dist;

            var s = (float) Mth.clamp(dist / 1.5, 0, 1);
            poseStack.scale(s, s, s);
        }

        if (ownerDist > 0) {
//            if (!shouldOffset && entity.sourceOffset != null) {
//                var d = 1 - Ease.outCubic((float)MathHelper.clamp(ownerDist / 15, 0, 1));
//                var posDiff = entity.sourceOffset.multiply(d);
//                matrices.translate(posDiff.x, posDiff.y, posDiff.z);
//            }

            poseStack.mulPose(new Quaternionf().rotationY(bYaw - Mth.PI / 2));
            poseStack.mulPose(new Quaternionf().rotationZ(bPitch - Mth.PI / 2));

            if (shouldOffset) {
                var side = 1;
//                if (sourceArm.get() == Arm.LEFT) side = -1;

                var d = 1 - Ease.outCubic((float)Mth.clamp(ownerDist / 15, 0, 1));
                poseStack.translate(0.2f * d, 0, 0.5f * d * side);
            }

            PlasmaRenderer.renderPlasma(poseStack, bufferSource, light, 0xFFFFFF, false, 1.5f, 1.0f, 1.0f, false, LightsaberContent.RED);
        }
        poseStack.popPose();
    }
}
