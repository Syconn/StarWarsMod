package mod.syconn.swm.features.lightsaber.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.client.item.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.utils.client.NodeVec3;
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
            final var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            final var rotation = facing.getAxis() == Direction.Axis.X ? Axis.ZN.rotationDegrees(90f * facing.getNormal().getX()) : Axis.XN.rotationDegrees(-90f * facing.getNormal().getZ());
            final var lT = LightsaberTag.getOrCreate(stack);
            final var emitterPos = lT.blades.isEmpty() ? new NodeVec3() : lT.blades.get(0).emitterPos;
            final var hiltLength = lT.hiltLength();
            final var scale = LightsaberItemRender.getScalar(stack);

            poseStack.pushPose();
            poseStack.translate(0.5f - emitterPos.x() * scale.x(), 1f - (emitterPos.y() - hiltLength / 2) * scale.y(), 0.5f - emitterPos.z() * scale.z());
            MathUtil.translateRotation(poseStack, facing, 0, 0, 0.5f);
            poseStack.rotateAround(rotation, (float) (emitterPos.x() * scale.x()), (float) ((emitterPos.y() - hiltLength / 2) * scale.y()), (float) (emitterPos.z() * scale.z()));
            poseStack.mulPose(MathUtil.getNorthRotation(facing.getCounterClockWise()));
            itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
