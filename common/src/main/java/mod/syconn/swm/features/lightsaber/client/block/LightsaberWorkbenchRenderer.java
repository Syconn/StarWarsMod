package mod.syconn.swm.features.lightsaber.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.utils.generic.MathUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LightsaberWorkbenchRenderer implements BlockEntityRenderer<LightsaberWorkbenchBlockEntity> {

    private final ItemRenderer itemRenderer;

    public LightsaberWorkbenchRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(LightsaberWorkbenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var stack = blockEntity.getContainer().getItem(0);

        if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
            var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            var emitterPos = LightsaberTag.getOrCreate(stack).blades.isEmpty() ? null : LightsaberTag.getOrCreate(stack).blades.get(0).emitterPos;

            poseStack.pushPose();
            poseStack.translate(0.5f, 1f, 0.5f);
            MathUtil.translateRotation(poseStack, facing.getClockWise(), 0.5f, 0f, 0f);
            if (emitterPos != null) MathUtil.translateRotation(poseStack, facing.getClockWise(),
                    (float) -emitterPos.y() + (float) LightsaberTag.getOrCreate(stack).hiltLength() / 2f, (float) emitterPos.x(), (float) emitterPos.z());
            poseStack.mulPose(facing.getAxis() == Direction.Axis.X ? Axis.ZN.rotationDegrees(90f * facing.getNormal().getX()) : Axis.XN.rotationDegrees(-90f * facing.getNormal().getZ()));
            itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
