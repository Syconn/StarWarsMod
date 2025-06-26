package mod.syconn.swm.client.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swm.util.client.GraphicsUtil;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ColoredScrollBar extends ScrollWidget {

    private final Function<Integer, Integer> packedHSV;

    public ColoredScrollBar(int x, int y, int width, int height, String prefix, double minValue, double maxValue, double currentValue, Function<Integer, Integer> packedHSV, OnChange onChange) {
        super(x, y, width, height, Component.literal(prefix), Component.empty(), minValue, maxValue, currentValue, true, onChange);
        this.packedHSV = packedHSV;
    }

    protected void updateMessage() {
        if (onChange != null) onChange.onChange(this);
    }

    protected void renderBackground(PoseStack poseStack) {
        GraphicsUtil.renderHSVSquare(poseStack, this.x, this.y, this.width, this.height, packedHSV);
    }
}
