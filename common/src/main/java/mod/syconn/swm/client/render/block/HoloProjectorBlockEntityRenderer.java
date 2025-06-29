package mod.syconn.swm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.client.render.entity.HologramRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        var mc = GameInstance.getClient();
        var hologram = new HologramRenderer(new EntityRendererProvider.Context(context.getEntityRenderer(), context.getItemRenderer(), context.getBlockRenderDispatcher(),
                context.getEntityRenderer().getItemInHandRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.font), false);

        hologram.render(ClientHooks.fakePlayer(mc.level, "test"), 0, partialTick, poseStack, buffer, packedLight);

        poseStack.popPose();
    }
}
