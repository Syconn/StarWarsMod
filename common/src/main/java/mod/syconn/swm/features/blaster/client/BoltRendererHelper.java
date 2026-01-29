package mod.syconn.swm.features.blaster.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import mod.syconn.swm.utils.client.PlasmaRenderer;
import mod.syconn.swm.utils.generic.AnimationUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
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

        var sourceArm = entity.getSourceArm();
        if (entity.tickCount == 0 && sourceArm.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0, 0.5f * entity.getBbHeight(), 0);

//        if (entity.sourceOffset == null && entity.getOwner() instanceof Player player) { TODO Barrel Extension
//            var socket = PlayerSocket.getSocket(player, BlasterItem.SOCKET_ID_BARREL_END);
//            if (socket != null) {
//                var source = socket.position();
//                var sourceOffset = new Vec3d(source.x, source.y, source.z).subtract(entity.getOwner().getEyePos());
//                if (sourceOffset.lengthSquared() < 1)
//                    entity.sourceOffset = sourceOffset;
//            }
//        }

        var mc = Minecraft.getInstance();
        var isOwnedByClient = entity.getOwner() == mc.player;
        var isFirstPerson = mc.options.getCameraType() == CameraType.FIRST_PERSON;
        var isCameraPlayer = isFirstPerson && mc.getCameraEntity() == mc.player;
        var shouldScale = isOwnedByClient && isFirstPerson && isCameraPlayer;
        var shouldOffset = shouldScale;

//        if (shouldScale) { TODO ADS offset
//            var mainStack = mc.player.getMainHandItem();
//            if (mainStack.getItem() instanceof BlasterItem) {
//                var bt = new BlasterTag(mainStack.getOrCreateNbt());
//                shouldOffset = !bt.isAimingDownSights;
//            }
//        }

        shouldOffset = shouldOffset && sourceArm.isPresent();

        var ownerDist = 1d;
        if (shouldScale) {
            var dist = mc.player.getEyePosition(partialTicks).distanceTo(entity.getPosition(partialTicks));
            ownerDist = dist;

            var s = (float) Mth.clamp(dist / 1.5, 0, 1);
            poseStack.scale(s, s, s);
        }

        if (ownerDist > 0) {
            if (!shouldOffset && entity.sourceOffset != null) {
                var d = 1 - AnimationUtil.outCubic((float) Mth.clamp(ownerDist / 15, 0, 1));
                var posDiff = entity.sourceOffset.scale(d);
                poseStack.translate(posDiff.x, posDiff.y, posDiff.z);
            }

            poseStack.mulPose(new Quaternionf().rotationY(bYaw - Mth.PI / 2));
            poseStack.mulPose(new Quaternionf().rotationZ(bPitch - Mth.PI / 2));

            if (shouldOffset) {
                var side = 1;
                if (sourceArm.get() == HumanoidArm.LEFT) side = -1;

                var d = 1 - AnimationUtil.outCubic((float) Mth.clamp(ownerDist / 15, 0, 1));
                poseStack.translate(0.2f * d, 0, 0.5f * d * side);
            }

            PlasmaRenderer.renderPlasma(poseStack, bufferSource, light, 0xFFFFFF, false, 1.5f, entity.getLength(), entity.getRadius(), false, entity.getColor(), true);
        }
        poseStack.popPose();
    }
}
