package mod.syconn.swm.features.lightsaber.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.util.math.DirectionUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class LightsaberWorkbenchRenderer implements BlockEntityRenderer<LightsaberWorkbenchBlockEntity> {

    // TODO
    //  Purely for modifying the blades qualities and components
    //  Not for building or constructing a lightsaber

    private final ItemRenderer itemRenderer;

    public LightsaberWorkbenchRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(LightsaberWorkbenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!blockEntity.getContainer().getItem(0).isEmpty() && blockEntity.getContainer().getItem(0).getItem() instanceof LightsaberItem) {
            var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            var pos = DirectionUtil.dataList(List.of(0.5f, -0.3f), List.of(0.5f, 1.3f), List.of(-0.3f, 0.5f), List.of(1.3f, 0.5f)).get(facing);

            poseStack.pushPose();
            poseStack.translate(pos.get(0), 1, pos.get(1));
            poseStack.mulPose(facing.getAxis() == Direction.Axis.X ? Axis.ZN.rotationDegrees(90f * facing.getNormal().getX()) : Axis.XN.rotationDegrees(-90f * facing.getNormal().getZ()));

            itemRenderer.renderStatic(blockEntity.getContainer().getItem(0), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);

            poseStack.popPose();
        }
    }
}
