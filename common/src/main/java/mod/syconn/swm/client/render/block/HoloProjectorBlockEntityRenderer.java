package mod.syconn.swm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import mod.syconn.swm.utils.client.HologramData;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoloProjectorBlockEntityRenderer implements BlockEntityRenderer<HoloProjectorBlockEntity> { // TODO REMOVAL DOESNT WORK
    // TODO SETTINGS MENU FOR (AREA, ENTITY TYPES, NON POSITIONAL CALLS), CENTER PLAYER WITH HANDHELD ON MIDDLE OF CALL
    //  ADD MULTI ENTITY SUPPORT, CHANGE IN VIEW RENDER LOGIC TO CERTAIN AREAS, ITEM NEED GRADIENT, REMOVE BLOCK FUNCTIONALITY,

    private final Map<UUID, HologramData> RENDERERS = new HashMap<>();

    public HoloProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HoloProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        for (var renderable : blockEntity.getRenderables().entrySet()) { // TODO PROBABLY NO LEAVE ANIMATION
            poseStack.pushPose();

            var hologramData = getHologramData(renderable.getKey(), renderable.getValue());  // TODO PARTIAL TICKS TRANSITION TO POSITION MAYBE FOR SMOOTHNESS
            poseStack.translate(hologramData.getPosition().x, hologramData.getPosition().y, hologramData.getPosition().z);
            hologramData.getRenderer().render(poseStack, buffer, partialTick, LightTexture.FULL_BLOCK);

            poseStack.popPose();
        }
    }

    private HologramData getHologramData(UUID entity, Vec3 pos) {
        var data = RENDERERS.get(entity);
        if (data == null) RENDERERS.put(entity, new HologramData(entity, pos, false));
        else if (!pos.equals(data.getPosition())) RENDERERS.put(entity, data.setPosition(pos));
        return RENDERERS.get(entity);
    }

    @Override
    public int getViewDistance() {
        return 32;
    }

    @Override
    public boolean shouldRenderOffScreen(HoloProjectorBlockEntity blockEntity) {
        return !blockEntity.getRenderables().isEmpty() && shouldRender(blockEntity, GameInstance.getClient().getCameraEntity().getEyePosition());
    }
}
