package mod.syconn.swm.client.screen.components;

import mod.syconn.swm.util.client.GraphicsUtil;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ColoredScrollBar extends ScrollWidget {

    private final Function<Integer, Integer> packedHSV;

    public ColoredScrollBar(int x, int y, int width, int height, String prefix, double minValue, double maxValue, double currentValue, Function<Integer, Integer> packedHSV) {
        super(x, y, width, height, Component.literal(prefix), Component.empty(), minValue, maxValue, currentValue, true, null);
        this.packedHSV = packedHSV;
    }

    protected void updateMessage() {
        var hsv = packedHSV != null ? packedHSV.apply((int) (getValueInt() / 355f * width)) : -1;
        var color = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(hsv), ColorUtil.hsvGetS(hsv), ColorUtil.hsvGetV(hsv));
        if (this.drawString) this.setMessage(Component.literal(this.getValueString()).withStyle(Component.literal("").getStyle().withColor(color)));
        else this.setMessage(Component.empty());
        if (onChange != null) onChange.onChange(this);
    }

    protected void renderBackground(GuiGraphics graphics) {
        GraphicsUtil.renderHSVSquare(graphics, this.getX(), this.getY(), this.width, this.height, packedHSV); //(k * (355f / 167f)) / 360f
    }
}
