package mod.syconn.swm.utils.interfaces;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public interface ICustomHudRenderer {

    HashMap<Class<? extends Item>, ICustomHudRenderer> REGISTRY = new HashMap<>();

    static void register(Class<? extends Item> item, ICustomHudRenderer renderer)
    {
        REGISTRY.put(item, renderer);
    }

    boolean renderCrosshair(GuiGraphics graphics, Player player, InteractionHand hand, ItemStack stack);

    void renderOverlay(GuiGraphics graphics, Player player, InteractionHand hand, ItemStack stack, int scaledWidth, int scaledHeight, float tickDelta);
}
