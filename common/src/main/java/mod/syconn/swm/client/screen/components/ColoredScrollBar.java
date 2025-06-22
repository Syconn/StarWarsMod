package mod.syconn.swm.client.screen.components;

import net.minecraft.network.chat.Component;

public class ColoredScrollBar extends ScrollWidget {

    public ColoredScrollBar(int x, int y, int width, int height, String prefix, double minValue, double maxValue, double currentValue, boolean drawString) {
        super(x, y, width, height, Component.literal(prefix), Component.empty(), minValue, maxValue, currentValue, drawString);
    }
}
