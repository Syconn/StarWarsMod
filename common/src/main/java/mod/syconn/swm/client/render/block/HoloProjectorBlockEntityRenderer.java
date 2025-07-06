package mod.syconn.swm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> {

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5f, 0.1f, 0.5f);
        if (blockEntity.getHologramData() != null) blockEntity.getHologramData().getRenderer().render(poseStack, buffer, partialTick, LightTexture.FULL_BLOCK);

        poseStack.popPose();
    }
}
