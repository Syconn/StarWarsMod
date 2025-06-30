package mod.syconn.swm.utils.client.render;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.utils.server.StackedIngredient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IngredientRenderer<T extends StackedIngredient> {
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

    public void display(GuiGraphics guiGraphics, int count, int x, int y, int mouseX, int mouseY) {
        if (this.displayStacks.size() > this.selectedStack) {
            guiGraphics.renderItem(this.displayStacks.get(this.selectedStack), x, y);
            if (count > 0) guiGraphics.renderItemDecorations(GameInstance.getClient().font, new ItemStack(this.displayStacks.get(this.selectedStack).getItem(), count), x, y);
            if (inBounds(x, y, mouseX, mouseY)) guiGraphics.renderTooltip(GameInstance.getClient().font, new ItemStack(this.displayStacks.get(this.selectedStack).getItem()), mouseX, mouseY);
        }
    }

    private boolean inBounds(int x, int y, int mouseX, int mouseY) {
        var size = 18;
        return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
}
