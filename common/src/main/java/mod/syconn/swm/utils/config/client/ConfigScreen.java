//package mod.syconn.swm.utils.config.client;
//
//import com.mojang.blaze3d.platform.InputConstants;
//import net.minecraft.Util;
//import net.minecraft.client.KeyMapping;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.network.chat.CommonComponents;
//import net.minecraft.network.chat.Component;
//
//public class ConfigScreen extends Screen { // TODO CREATE ME
//
//    public KeyMapping selectedKey;
//    public long lastKeySelection;
//    private ConfigList configList;
//    private Button resetButton;
//
//    public ConfigScreen() {
//        super(Component.literal("Config Screen"));
//    }
//
//    protected void init() {
//        this.configList = new HeroKeybindsList(this, this.minecraft);
//        this.addWidget(this.configList);
//        this.resetButton = this.addRenderableWidget(Button.builder(Component.translatable("controls.resetAll"), (button) -> {
//            for(KeyMapping keyMapping : this.configManager.unregisteredKeys()) keyMapping.setKey(keyMapping.getDefaultKey());
//            this.configList.resetMappingAndUpdateButtons();
//        }).bounds(this.width / 2 - 155, this.height - 29, 150, 20).build());
//        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).bounds(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
//    }
//
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (this.selectedKey != null) {
//            this.configManager.setKey(this.selectedKey, InputConstants.Type.MOUSE.getOrCreate(button));
//            this.selectedKey = null;
//            this.configList.resetMappingAndUpdateButtons();
//            return true;
//        } else {
//            return super.mouseClicked(mouseX, mouseY, button);
//        }
//    }
//
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (this.selectedKey != null) {
//            if (keyCode == 256) this.configManager.setKey(this.selectedKey, InputConstants.UNKNOWN);
//            else this.configManager.setKey(this.selectedKey, InputConstants.getKey(keyCode, scanCode));
//
//            this.selectedKey = null;
//            this.lastKeySelection = Util.getMillis();
//            this.configList.resetMappingAndUpdateButtons();
//            return true;
//        } else {
//            return super.keyPressed(keyCode, scanCode, modifiers);
//        }
//    }
//
//    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
//
//        this.configList.render(guiGraphics, mouseX, mouseY, partialTick);
//        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 16777215);
//        boolean bl = false;
//
//        for(KeyMapping keyMapping : this.configManager.unregisteredKeys()) {
//            if (!keyMapping.isDefault()) {
//                bl = true;
//                break;
//            }
//        }
//
//        this.resetButton.active = bl;
//        super.render(guiGraphics, mouseX, mouseY, partialTick);
//    }
//}
