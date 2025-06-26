package mod.syconn.swm.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.util.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ColoredLightsaberButton extends ExpandedButton {

    private static final ResourceLocation COMPONENTS = Constants.withId("textures/gui/components.png");

    private final int hsv;
    private final int u, v;

    public ColoredLightsaberButton(int xPos, int yPos, String displayString, int hsv, int u, int v, OnPress handler) {
        this(xPos, yPos, Component.literal(displayString), hsv, u, v, handler);
    }

    public ColoredLightsaberButton(int xPos, int yPos, Component displayString, int hsv, int u, int v, OnPress handler) {
        super(xPos, yPos, 18, 18, displayString, handler);
        this.hsv = hsv;
        this.u = u;
        this.v = v;
    }

    public void render(PoseStack poseStack, int i, int j, float f) {
        poseStack.pushPose();

        RenderSystem.setShaderTexture(0, COMPONENTS);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.scale(0.25f, 0.25f, 0.25f);
        blit(poseStack, this.x * 4, this.y * 4, 71 * u, 24 + 71 * v, 71, 71);

        poseStack.popPose();
    }

    public int getHSV() {
        return hsv;
    }
}
