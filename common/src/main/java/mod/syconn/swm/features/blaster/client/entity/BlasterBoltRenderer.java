package mod.syconn.swm.features.blaster.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.features.blaster.client.BoltRendererHelper;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlasterBoltRenderer extends EntityRenderer<BlasterBoltEntity> {

    public BlasterBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BlasterBoltEntity entity) {
        return new ResourceLocation("missing");
    }

    public void render(BlasterBoltEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        BoltRendererHelper.renderStun(entity, poseStack, buffer, packedLight, partialTick);
    }
}
