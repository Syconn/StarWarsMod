package mod.syconn.swm.features.lightsaber.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
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
        if (!blockEntity.getContainer().getItem(0).isEmpty() && blockEntity.getContainer().getItem(0).getItem() instanceof LightsaberItem) {
            var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            var lT = LightsaberTag.getOrCreate(blockEntity.getContainer().getItem(0));
            var emitter = lT.emitterPositions.get(0);

            var x = 1.25; // (facing.getAxis() == Direction.Axis.X ? 0.2 * facing.getNormal().getX() : 0.5) + (facing == Direction.EAST ? 1 : 0);
            var z = facing.getAxis() == Direction.Axis.X ? 0.5 : 1 - 0.2 * facing.getNormal().getZ();
            var rot = 90f; //* (facing.getAxis() == Direction.Axis.X ? facing.getNormal().getX() : facing.getNormal().getZ());

            poseStack.pushPose();
            poseStack.translate(x, 1, z);
            poseStack.translate(emitter.x, 0, emitter.z);
            poseStack.mulPose(facing.getAxis() == Direction.Axis.X ? Axis.ZN.rotationDegrees(rot) : Axis.XN.rotationDegrees(rot));

            itemRenderer.renderStatic(lT.getTemporary(false), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(),0);

            poseStack.popPose();
        }
    }
}
