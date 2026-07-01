package mod.syconn.swm.features.blaster.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.features.blaster.item.BlasterItem;
import mod.syconn.swm.features.blaster.server.data.BlasterTag;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.PlasmaBuffer;
import mod.syconn.swm.utils.client.TintedResourceLocation;
import mod.syconn.swm.utils.generic.AnimationUtil;
import mod.syconn.swm.utils.generic.ColorUtil;
import mod.syconn.swm.utils.generic.ModelUtil;
import mod.syconn.swm.utils.interfaces.IModifiedItemRenderer;
import mod.syconn.swm.utils.interfaces.IModifiedPoseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BlasterItemRenderer implements IModifiedItemRenderer, IModifiedPoseRenderer {

    private static final ResourceLocation[] ID_MUZZLE_FLASHES_FORWARD = new ResourceLocation[] {
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_forward_4.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_forward_0.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_forward_1.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_forward_2.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_forward_3.png")
    };

    private static final ResourceLocation[] ID_MUZZLE_FLASHES = new ResourceLocation[] {
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_9.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_0.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_5.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_7.png"),
            Constants.withId("textures/item/model/blaster/effect/muzzleflash_9.png")
    };

    public boolean render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel backupModel) {
        poseStack.pushPose();

        var model = IModifiedItemRenderer.getModel(BlasterTag.getOrCreate(stack).model, backupModel);
        var transforms = model.getTransforms();
        transforms.getTransform(renderMode).apply(leftHanded, poseStack);
        renderItemModel(stack, poseStack, buffer, light, overlay, model);

        if (renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.FIXED && renderMode != ItemDisplayContext.GROUND) {
            var bt = BlasterTag.getOrCreate(stack);
            var d = StarWarsClient.getTickDelta();
            var shotTime = bt.timeSinceLastShot + d;

            poseStack.pushPose();

            poseStack.mulPoseMatrix(bt.muzzle.firePoint.matrix4f());
            renderMuzzleFlash(renderMode, poseStack, buffer, bt, shotTime, light, overlay);

            poseStack.popPose();
        }

        poseStack.popPose();
        return true;
    }

    private void renderMuzzleFlash(ItemDisplayContext renderMode, PoseStack poseStack, MultiBufferSource buffer, BlasterTag bt, float shotTime, int light, int overlay) {
        shotTime *= 1.2f;
        VertexConsumer vc;
        float opacity = Mth.clamp(1 - (float) Math.pow(shotTime / (ID_MUZZLE_FLASHES.length - 1), 2), 0, 1);

        if (opacity > 0) {
            var frame = (int)Math.floor(Mth.clamp(shotTime, 0, ID_MUZZLE_FLASHES.length - 1));
            var flashColor = bt.muzzle.color;

            var mode = bt.muzzle.projectileType;
            if (mode.equals(BlasterContent.STUN)) flashColor = ColorUtil.packHsv(0.6f, 1, 1); // || mode.equals(BlasterContent.ION

            var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(flashColor), ColorUtil.hsvGetS(flashColor), ColorUtil.hsvGetV(flashColor));
            var tintedId = new TintedResourceLocation(ID_MUZZLE_FLASHES[frame], ColorUtil.argbToAbgr(color), ColorUtil.TintMode.Overlay);
            var tintedForwardId = new TintedResourceLocation(ID_MUZZLE_FLASHES_FORWARD[frame], ColorUtil.argbToAbgr(color), ColorUtil.TintMode.Overlay);

            var flash = StarWarsClient.tintedTextureProvider.getId("muzzleflash/" + ColorUtil.toResourceId(color) + "/" + frame, () -> ID_MUZZLE_FLASHES[frame], () -> tintedId);

            vc = buffer.getBuffer(getMuzzleFlashLayer(flash));
            PlasmaBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, opacity, overlay, light);

            final var flashRadius = 0.25f; //.45

            PlasmaBuffer.RENDER.vertex(-flashRadius, -flashRadius, 0, 0, 0, 1, 0, 0);
            PlasmaBuffer.RENDER.vertex(flashRadius, -flashRadius, 0, 0, 0, 1, 15 / 16f, 0);
            PlasmaBuffer.RENDER.vertex(flashRadius, flashRadius, 0, 0, 0, 1, 15 / 16f, 15 / 16f);
            PlasmaBuffer.RENDER.vertex(-flashRadius, flashRadius, 0, 0, 0, 1, 0, 15 / 16f);

            if (!renderMode.firstPerson()) {
                var forwardFlash = StarWarsClient.tintedTextureProvider.getId("muzzleflash_forward/" + ColorUtil.toResourceId(color) + "/" + frame, () -> ID_MUZZLE_FLASHES_FORWARD[frame], () -> tintedForwardId);
                vc = buffer.getBuffer(getMuzzleFlashLayer(forwardFlash));
                PlasmaBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, opacity, overlay, light);

                final var maxU = 30 / 32f;
                final var maxV = 15 / 32f;

                // vertical
                PlasmaBuffer.RENDER.vertex(0, -flashRadius, -0.2f, 0, 0, 1, maxU, 0);
                PlasmaBuffer.RENDER.vertex(0, -flashRadius, -0.2f + 3 * flashRadius, 0, 0, 1, 0, 0);
                PlasmaBuffer.RENDER.vertex(0, flashRadius, -0.2f + 3 * flashRadius, 0, 0, 1, 0, maxV);
                PlasmaBuffer.RENDER.vertex(0, flashRadius, -0.2f, 0, 0, 1, maxU, maxV);

                // horizontal
                PlasmaBuffer.RENDER.vertex(-flashRadius, 0, -0.2f, 0, 0, 1, maxU, 0);
                PlasmaBuffer.RENDER.vertex(-flashRadius, 0, -0.2f + 3 * flashRadius, 0, 0, 1, 0, 0);
                PlasmaBuffer.RENDER.vertex(flashRadius, 0, -0.2f + 3 * flashRadius, 0, 0, 1, 0, maxV);
                PlasmaBuffer.RENDER.vertex(flashRadius, 0, -0.2f, 0, 0, 1, maxU, maxV);
            }
        }
    }

    private static RenderType getMuzzleFlashLayer(ResourceLocation texture) {
        return RenderType.create("swm:muzzle_flash2", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(RenderStateShard.BLOCK_SHEET)
                        .setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    public void modifyPose(LivingEntity entity, InteractionHand hand, ItemStack stack, HumanoidModel<? extends LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float tickDelta) {
        var hold = BlasterItem.getHold(entity);
        var bt = BlasterTag.getOrCreate(stack);
        var mainArm = entity.getMainArm();
        var mc = Minecraft.getInstance();

        boolean patrol = false;

        if (!hold.isDefaultHand(hand) || !hold.hasBlaster || (entity == mc.cameraEntity && mc.options.getCameraType().isFirstPerson())) return;

        // TODO: This can probably be simplified, hold closer?

        if (mc.player == entity && mc.options.getCameraType().isFirstPerson() && hand == InteractionHand.OFF_HAND) return;

        switch (hold) {
            case ONE_HANDED_MAIN -> {
                if (mainArm == HumanoidArm.LEFT) poseSingleLeft(entity, bt, patrol, model, limbSwing, limbSwingAmount);
                else poseSingleRight(entity, bt, patrol, model, limbSwing, limbSwingAmount);
            }
            case ONE_HANDED_OFF -> {
                if (mainArm == HumanoidArm.RIGHT) poseSingleLeft(entity, bt, patrol, model, limbSwing, limbSwingAmount);
                else poseSingleRight(entity, bt, patrol, model, limbSwing, limbSwingAmount);
            }
            case TWO_HANDED_MAIN -> {
                if (mainArm == HumanoidArm.LEFT) poseDoubleLeft(entity, bt, patrol, model, limbSwing, limbSwingAmount, ageInTicks);
                else poseDoubleRight(entity, bt, patrol, model, limbSwing, limbSwingAmount, ageInTicks);
            }
            case TWO_HANDED_OFF -> {
                if (mainArm == HumanoidArm.RIGHT) poseDoubleLeft(entity, bt, patrol, model, limbSwing, limbSwingAmount, ageInTicks);
                else poseDoubleRight(entity, bt, patrol, model, limbSwing, limbSwingAmount, ageInTicks);
            }
            case DUAL -> poseDual(entity, bt, patrol, model, limbSwing, limbSwingAmount, ageInTicks);
        }
    }

    private void poseSingleLeft(LivingEntity entity, BlasterTag bt, boolean patrol, HumanoidModel<? extends LivingEntity> model, float limbAngle, float limbDistance) { // Patrol is walking with blaster mode
        var limbBounce = limbDistance * Mth.sin(limbAngle / 2f) * 0.05f;
        if (patrol) {
            ModelUtil.lerpLeftArmToDegrees(model, 1, -1.436f + limbBounce, 0.808f, -0.269f);
            ModelUtil.lerpRightArmToDegrees(model, 1, -1.077f - limbBounce, 0, -0.539f);
        } else {
            var ads = bt.ads;

            if (ads) {
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.xRot = -1.5F + model.head.xRot - limbBounce;

                model.leftArm.xRot = -1.6F + model.head.xRot + limbBounce;
            }
            else model.leftArm.xRot = -1.35F + model.head.xRot + limbBounce;

            model.leftArm.yRot = 0.1F + model.head.yRot;

            var lerp = Mth.clamp(AnimationUtil.outCubic((float)Math.pow(limbDistance / 0.8, 2)), 0, 1);
            ModelUtil.lerpLeftArmToDegrees(model, lerp, -1.436f + limbBounce, 0.808f, -0.269f);
            ModelUtil.lerpRightArmToDegrees(model, lerp, -1.077f - limbBounce, 0, -0.539f);
        }
    }

    private void poseSingleRight(LivingEntity entity, BlasterTag bt, boolean patrol, HumanoidModel<? extends LivingEntity> model, float limbAngle, float limbDistance) { // TODO UPDATE THIS
        var limbBounce = limbDistance * Mth.sin(limbAngle / 2f) * 0.05f;
        if (patrol) {
            ModelUtil.lerpLeftArmToDegrees(model, 1, -1.077f - limbBounce, 0, 0.539f);
            ModelUtil.lerpRightArmToDegrees(model, 1, -1.436f + limbBounce, -0.808f, 0.269f);
        } else {
            var ads = bt.ads;

            if (ads) {
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.xRot = -1.5F + model.head.xRot + limbBounce;

                model.rightArm.xRot = -1.6F + model.head.xRot - limbBounce;
            } else model.rightArm.xRot = -1.35F + model.head.xRot - limbBounce;

            model.rightArm.yRot = -0.1F + model.head.yRot;

            var lerp = Mth.clamp(AnimationUtil.outCubic((float)Math.pow(limbDistance / 0.8, 2)), 0, 1);
            ModelUtil.lerpLeftArmToDegrees(model, lerp, -1.077f - limbBounce, 0, 0.539f);
            ModelUtil.lerpRightArmToDegrees(model, lerp, -1.436f + limbBounce, -0.808f, 0.269f);
        }
    }

    private void poseDoubleLeft(LivingEntity entity, BlasterTag bt, boolean patrol, HumanoidModel<? extends LivingEntity> model, float limbAngle, float limbDistance, float animationProgress) {
        var limbBounce = limbDistance * Mth.sin(limbAngle / 2f) * 0.05f;
        var breatheBounceLeft = Mth.sin(animationProgress / 15f + entity.getId()) * 0.01f;
        var breatheBounceRight = Mth.cos(animationProgress / 15f - entity.getId()) * 0.01f;

        if (patrol) {
            ModelUtil.lerpRightArmToDegrees(model, 1, -0.539f - limbBounce + breatheBounceLeft, 0.269f, -0.09f);
            ModelUtil.lerpLeftArmToDegrees(model, 1, -0.808f + limbBounce + breatheBounceRight, 1.077f, 0);
        } else {
            var ads = bt.ads;
            var xRotChange = ads ? 0.4f : 0;

            model.leftArm.yRot = 0.15F + model.head.yRot;
            model.leftArm.xRot = -1.57F + model.head.xRot - xRotChange;

            model.rightArm.yRot = model.head.yRot - 0.785398F;
            model.rightArm.xRot = -1.47F + model.head.xRot - xRotChange;

            if (ads) model.head.zRot = 0.2f;
        }
    }

    private void poseDoubleRight(LivingEntity entity, BlasterTag bt, boolean patrol, HumanoidModel<? extends LivingEntity> model, float limbAngle, float limbDistance, float animationProgress) {
        var limbBounce = limbDistance * Mth.sin(limbAngle / 2f) * 0.05f;
        var breatheBounceLeft = Mth.sin(animationProgress / 15f + entity.getId()) * 0.01f;
        var breatheBounceRight = Mth.cos(animationProgress / 15f - entity.getId()) * 0.01f;

        if (patrol) {
            ModelUtil.lerpLeftArmToDegrees(model, 1, -0.539f - limbBounce + breatheBounceLeft, -0.269f, 0.09f);
            ModelUtil.lerpRightArmToDegrees(model, 1, -0.808f + limbBounce + breatheBounceRight, -1.077f, 0);
        } else {
            var ads = bt.ads;
            var xRotChange = ads ? 0.4f : 0;

            model.rightArm.yRot = -0.15F + model.head.yRot;
            model.rightArm.xRot = -1.57F + model.head.xRot - xRotChange;

            model.leftArm.yRot = model.head.yRot + 0.785398F;
            model.leftArm.xRot = -1.47F + model.head.xRot - xRotChange;

            if (ads) model.head.zRot = -0.2f;
        }
    }

    private void poseDual(LivingEntity entity, BlasterTag bt, boolean patrol, HumanoidModel<? extends LivingEntity> model, float limbAngle, float limbDistance, float animationProgress) {
        var breatheBounceLeft = Mth.sin(animationProgress / 15f + entity.getId()) * 0.01f;
        var breatheBounceRight = Mth.cos(animationProgress / 15f - entity.getId()) * 0.01f;
        var lerp = Mth.clamp(AnimationUtil.outCubic((float)Math.pow(limbDistance / 0.8, 2)), 0, 1);
        var limbBounce = limbDistance * Mth.sin(limbAngle / 2f) * 0.1f;

        if (patrol) {
            ModelUtil.lerpLeftArmToDegrees(model, 1 - lerp, -2.424f + limbBounce + breatheBounceLeft, -0.18f, 0.09f);
            ModelUtil.lerpRightArmToDegrees(model, 1 - lerp, -2.424f - limbBounce + breatheBounceRight, 0.18f, -0.09f);
        } else {
            ModelUtil.lerpLeftArmToDegrees(model, 1, -1.526f + breatheBounceLeft, 0, -0.18f);
            ModelUtil.lerpRightArmToDegrees(model, 1, -1.526f + breatheBounceRight, 0, 0.18f);

            ModelUtil.lerpLeftArmToDegrees(model, lerp, -0.718f + limbBounce + breatheBounceLeft, 0.09f, 0.359f);
            ModelUtil.lerpRightArmToDegrees(model, lerp, -0.628f - limbBounce + breatheBounceRight, -0.539f, -0.359f);
        }
    }
}
