package mod.syconn.swm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
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
    // TODO SETTINGS MENU FOR (AREA, ENTITY TYPES, NON POSITIONAL CALLS), CENTER PLAYER WITH HANDHELD ON MIDDLE OF CALL, BlockEntities in Block Range Should Not be Rendered
    //  ADD MULTI ENTITY SUPPORT, CHANGE IN VIEW RENDER LOGIC TO CERTAIN AREAS, ITEM NEED GRADIENT

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        for (var holo : blockEntity.getHolograms()) {
            poseStack.pushPose();

            var pos = holo.getPlayer().position();
//            poseStack.translate(0.5f, 0.1f, 0.5f);
            poseStack.translate(pos.x - blockEntity.getBlockPos().getX(), pos.y - blockEntity.getBlockPos().getY(), pos.z - blockEntity.getBlockPos().getZ());
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
