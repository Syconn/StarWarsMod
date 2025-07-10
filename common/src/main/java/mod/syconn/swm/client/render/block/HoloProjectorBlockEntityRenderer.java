package mod.syconn.swm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> { // TODO REMOVAL DOESNT WORK
    // TODO SETTINGS MENU FOR (AREA, ENTITY TYPES, NON POSITIONAL CALLS), CENTER PLAYER WITH HANDHELD ON MIDDLE OF CALL
    //  ADD MULTI ENTITY SUPPORT, CHANGE IN VIEW RENDER LOGIC TO CERTAIN AREAS, ITEM NEED GRADIENT, REMOVE BLOCK FUNCTIONALITY

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        for (var holo : blockEntity.getHolograms()) {
            poseStack.pushPose();

            poseStack.translate(holo.getPosition().x, holo.getPosition().y, holo.getPosition().z);
            holo.getRenderer().render(poseStack, buffer, partialTick, LightTexture.FULL_BLOCK);

            poseStack.popPose();
        }
    }

    @Override
    public int getViewDistance() {
        return 32;
    }

    @Override
    public boolean shouldRenderOffScreen(HoloProjectorBlockEntity blockEntity) {
        return !blockEntity.getHolograms().isEmpty() && shouldRender(blockEntity, GameInstance.getClient().getCameraEntity().getEyePosition());
    }
}
