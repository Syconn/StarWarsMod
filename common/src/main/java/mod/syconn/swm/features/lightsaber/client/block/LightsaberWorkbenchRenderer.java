package mod.syconn.swm.features.lightsaber.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LightsaberWorkbenchRenderer implements BlockEntityRenderer<LightsaberWorkbenchBlockEntity> {

    private final ItemRenderer itemRenderer;

    public LightsaberWorkbenchRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(LightsaberWorkbenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var stack = blockEntity.getContainer().getItem(0);

        if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem) {
            final var lT = LightsaberTag.getOrCreate(stack);
            final var emitterPos = lT.blades.isEmpty() ? new NodeVec3() : lT.blades.get(0).emitterPos;
            final var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            final var flatRotation = facing.getAxis() == Direction.Axis.X ? Axis.ZN.rotationDegrees(90f * facing.getNormal().getX()) : Axis.XN.rotationDegrees(-90f * facing.getNormal().getZ());

            poseStack.pushPose();
            poseStack.translate(0.5f - emitterPos.x(), 1f - emitterPos.y(), 0.5f - emitterPos.z());
            MathUtil.translateRotation(poseStack, facing, 0, 0, 0.235f + (float) (lT.hiltLength()));
            poseStack.rotateAround(flatRotation, 0, (float) emitterPos.y(), 0);
            poseStack.mulPose(MathUtil.getNorthRotation(facing));
            itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
