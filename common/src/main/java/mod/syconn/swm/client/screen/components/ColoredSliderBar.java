package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.utils.client.GraphicsUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ColoredSliderBar extends SliderWidget {

    private final Function<Integer, Integer> packedHSV;

    public ColoredSliderBar(int x, int y, int width, int height, String prefix, double minValue, double maxValue, double currentValue, Function<Integer, Integer> packedHSV, OnChange onChange) {
        super(x, y, width, height, Component.literal(prefix), Component.empty(), minValue, maxValue, currentValue, true, onChange);
        this.packedHSV = packedHSV;
    }

    protected void updateMessage() {
        if (onChange != null) onChange.onChange(this);
    }

    protected void renderBackground(GuiGraphics graphics) {
        GraphicsUtil.renderHSVSquare(graphics, this.getX(), this.getY(), this.width, this.height, packedHSV);
    }
}
