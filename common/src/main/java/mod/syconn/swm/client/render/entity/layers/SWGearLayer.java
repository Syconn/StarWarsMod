package mod.syconn.swm.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.server.data.SWGear;
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

public class SWGearLayer<P extends Player, M extends PlayerModel<P>> extends RenderLayer<P, M> {

    private final ItemRenderer itemRenderer;

    public SWGearLayer(RenderLayerParent<P, M> renderer, ItemRenderer itemRenderer) {
        super(renderer);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Player player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();

        var stack = ((SWGear.SWGearAccess) player).swm$getSWGear().getItemFromSlot(IEquipmentItem.SWEquipmentSlot.LIGHTSABER);
        if (player instanceof AbstractClientPlayer clientPlayer && !stack.isEmpty()) {
            poseStack.translate(clientPlayer.getMainArm() == HumanoidArm.RIGHT ? 0.3f : -0.3f, 1f, 0f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            this.itemRenderer.renderStatic(LightsaberTag.getTemporary(stack, false), ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.level(), player.getId());
        }

        poseStack.popPose();
    }
}
