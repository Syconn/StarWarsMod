package mod.syconn.swm.api.util;

import net.minecraft.client.gui.GuiGraphics;

public interface IHudOverlay {
    void draw(GuiGraphics graphics, float partialTick);
}
