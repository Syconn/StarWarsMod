package mod.syconn.swm.util.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.util.server.StackedIngredient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IngredientRenderer<T extends StackedIngredient> {

    private final ItemRenderer itemRenderer = GameInstance.getClient().getItemRenderer();
    private final List<ItemStack> displayStacks;
    private final T ingredient;
    private int selectedStack = 0;
    private long lastTime = System.currentTimeMillis();

    public IngredientRenderer(T ingredient) {
        this.displayStacks = List.of(ingredient.ingredient().getItems());
        this.ingredient = ingredient;
    }

    public T getIngredient() {
        return ingredient;
    }

    public void tick() {
        var currentTime = System.currentTimeMillis();
        if (currentTime - this.lastTime >= 1000) {
            if (!this.displayStacks.isEmpty()) this.selectedStack = (this.selectedStack + 1) % this.displayStacks.size();
            this.lastTime = currentTime;
        }
    }

    public void display(int count, int x, int y) {
        var stack = new ItemStack(this.displayStacks.get(this.selectedStack).getItem(), count);
        if (this.displayStacks.size() > this.selectedStack) {
            this.itemRenderer.renderGuiItem(stack, x, y);
            if (count > 0) this.itemRenderer.renderGuiItemDecorations(GameInstance.getClient().font, stack, x, y);
        }
    }

    public void renderTooltip(Screen screen, PoseStack poseStack, int x, int y, int mouseX, int mouseY) {
        var stack = new ItemStack(this.displayStacks.get(this.selectedStack).getItem());
        if (inBounds(x, y, mouseX, mouseY)) screen.renderTooltip(poseStack, screen.getTooltipFromItem(stack), stack.getTooltipImage(), mouseX, mouseY);
    }

    private boolean inBounds(int x, int y, int mouseX, int mouseY) {
        var size = 18;
        return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
}
