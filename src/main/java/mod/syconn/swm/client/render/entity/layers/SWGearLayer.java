package mod.syconn.swm.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.client.item.LightsaberItemRender;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.server.containers.SWGear;
import mod.syconn.swm.utils.client.NodeVec3;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;

public class SWGearLayer<P extends Player, M extends PlayerModel<P>> extends RenderLayer<P, M> {

    private final ItemRenderer itemRenderer;

    public SWGearLayer(RenderLayerParent<P, M> renderer, ItemRenderer itemRenderer) {
        super(renderer);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull Player player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();

        if (player instanceof SWGear.SWGearAccess access) {
            var stack = access.swm$getSWGear().getItemFromSlot(IEquipmentItem.SWEquipmentSlot.LIGHTSABER);
            if (player instanceof AbstractClientPlayer clientPlayer && stack.getItem() instanceof LightsaberItem) {
                final var emitterPos = LightsaberTag.getOrCreate(stack).blades.isEmpty() ? new NodeVec3() : LightsaberTag.getOrCreate(stack).blades.get(0).emitterPos;
                final var hiltLength = LightsaberTag.getOrCreate(stack).hiltLength();
                final var scale = LightsaberItemRender.getScalar(stack);

                poseStack.translate((clientPlayer.getMainArm() == HumanoidArm.RIGHT ? 0.28f : -0.28f) - emitterPos.x() * scale.x, 0.8f + (float) (hiltLength - emitterPos.y() - hiltLength / 2) * scale.y, -emitterPos.z() * scale.z);
                if (player.isCrouching()) poseStack.translate(0f, 0f, 0.25f);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                this.itemRenderer.renderStatic(LightsaberTag.getTemporary(stack, false), ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.level(), player.getId());
            }
        }

        poseStack.popPose();
    }
}
