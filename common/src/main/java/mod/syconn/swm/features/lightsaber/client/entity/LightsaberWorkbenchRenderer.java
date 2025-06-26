package mod.syconn.swm.features.lightsaber.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.math.DirectionUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class LightsaberWorkbenchRenderer implements BlockEntityRenderer<LightsaberWorkbenchBlockEntity> {

    private final ItemRenderer itemRenderer;

    public LightsaberWorkbenchRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(LightsaberWorkbenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var stack = blockEntity.getContainer().getItem(0);

        if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
            var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            var pos = DirectionUtil.dataList(List.of(0.5f, -0.2f), List.of(0.5f, 1.2f), List.of(-0.2f, 0.5f), List.of(1.2f, 0.5f)).get(facing);

            poseStack.pushPose();
            poseStack.translate(pos.get(0), 1, pos.get(1));
            poseStack.mulPose(facing.getAxis() == Direction.Axis.X ? Vector3f.ZN.rotationDegrees(90f * facing.getNormal().getX()) : Vector3f.XN.rotationDegrees(-90f * facing.getNormal().getZ()));

            LightsaberContent.renderFixes(ItemTransforms.TransformType.NONE, poseStack, stack);
            itemRenderer.renderStatic(stack, ItemTransforms.TransformType.NONE, packedLight, packedOverlay, poseStack, buffer, 0);

            poseStack.popPose();
        }
    }
}
