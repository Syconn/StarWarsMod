package mod.syconn.swm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> {
    // TODO POSITIONAL LOGIC FOR RENDERING, SETTINGS MENU FOR (AREA, ENTITY TYPES, NON POSITIONAL CALLS), CENTER PLAYER WITH HANDHELD ON MIDDLE OF CALL,
    //  ADD MULTI ENTITY SUPPORT, CHANGE IN VIEW RENDER LOGIC TO CERTAIN AREAS

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5f, 0.1f, 0.5f);

        blockEntity.getHolograms().forEach(holo -> holo.getRenderer().render(poseStack, buffer, partialTick, LightTexture.FULL_BLOCK));

        poseStack.popPose();
    }
}
