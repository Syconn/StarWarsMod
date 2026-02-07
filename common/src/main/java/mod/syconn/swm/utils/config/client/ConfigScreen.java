package mod.syconn.swm.utils.config.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private KeyMapping selectedKey;
    private long lastKeySelection;
    private HeroKeybindsList keyBindsList;
    private Button resetButton;

    public ConfigScreen() {
        super(Component.literal("TEST"));
        this.manager = Minecraft.getInstance().player.herocore$getManager();
    }

    protected void init() {
        this.keyBindsList = new HeroKeybindsList(this, this.minecraft);
        this.addWidget(this.keyBindsList);
        this.resetButton = this.addRenderableWidget(Button.builder(Component.translatable("controls.resetAll"), (button) -> {
            for(KeyMapping keyMapping : this.manager.unregisteredKeys()) keyMapping.setKey(keyMapping.getDefaultKey());
            this.keyBindsList.resetMappingAndUpdateButtons();
        }).bounds(this.width / 2 - 155, this.height - 29, 150, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).bounds(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.selectedKey != null) {
            this.manager.setKey(this.selectedKey, InputConstants.Type.MOUSE.getOrCreate(button));
            this.selectedKey = null;
            this.keyBindsList.resetMappingAndUpdateButtons();
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.selectedKey != null) {
            if (keyCode == 256) this.manager.setKey(this.selectedKey, InputConstants.UNKNOWN);
            else this.manager.setKey(this.selectedKey, InputConstants.getKey(keyCode, scanCode));

            this.selectedKey = null;
            this.lastKeySelection = Util.getMillis();
            this.keyBindsList.resetMappingAndUpdateButtons();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        this.keyBindsList.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 16777215);
        boolean bl = false;

        for(KeyMapping keyMapping : this.manager.unregisteredKeys()) {
            if (!keyMapping.isDefault()) {
                bl = true;
                break;
            }
        }

        this.resetButton.active = bl;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
