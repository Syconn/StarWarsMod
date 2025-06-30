package mod.syconn.swm.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class HologramRenderer extends PlayerRenderer {

    private static final Minecraft mc = GameInstance.getClient();

    public HologramRenderer(BlockEntityRendererProvider.Context context, boolean useSlimModel) {
        super(new EntityRendererProvider.Context(context.getEntityRenderer(), context.getItemRenderer(), context.getBlockRenderDispatcher(), context.getEntityRenderer().getItemInHandRenderer(),
                mc.getResourceManager(), mc.getEntityModels(), mc.font), useSlimModel);
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float entityYaw, float partialTicks, int packedLight) {

    }
}
