package mod.syconn.swm.utils.config.client;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import mod.syconn.swm.utils.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private final ConfigScreen configScreen;
    private int maxNameWidth;

    public ConfigList(ConfigScreen configScreen, Minecraft minecraft) {
        super(minecraft, configScreen.width + 45, configScreen.height, 20, configScreen.height - 32, 20);
        this.configScreen = configScreen;
        KeyMapping[] keyMappings = ArrayUtils.clone(ConfigManager.MAPPINGS.toArray(new KeyMapping[0]));
        Arrays.sort(keyMappings);
        String string = null;

        for(KeyMapping keyMapping : keyMappings) {
            String string2 = keyMapping.getCategory();
            if (!string2.equals(string)) {
                string = string2;
                this.addEntry(new CategoryEntry(Component.translatable(string2)));
            }

            Component component = Component.translatable(keyMapping.getName());
            int i = minecraft.font.width(component);
            if (i > this.maxNameWidth) this.maxNameWidth = i;
            this.addEntry(new KeyEntry(this, keyMapping, component));
        }
    }

    public void resetMappingAndUpdateButtons() {
        KeyMapping.resetMapping();
        this.refreshEntries();
    }

    public void refreshEntries() {
        this.children().forEach(Entry::refreshEntry);
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 15;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        abstract void refreshEntry();
    }

    @Environment(EnvType.CLIENT)
    static class CategoryEntry extends Entry {
        final Component name;
        private final int width;

        public CategoryEntry(Component name) {
            this.name = name;
            this.width = minecraft.font.width(this.name);
        }

        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            Font var10001 = minecraft.font;
            int var10003 = minecraft.screen.width / 2 - this.width / 2;
            int var10004 = top + height;
            Objects.requireNonNull(minecraft.font);
            guiGraphics.drawString(var10001, this.name, var10003, var10004 - 9 - 1, 16777215, false);
        }

        @Nullable
        public ComponentPath nextFocusPath(FocusNavigationEvent event) {
            return null;
        }

        public @NotNull List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                public NarratableEntry.@NotNull NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }

                public void updateNarration(NarrationElementOutput narrationElementOutput) {
                    narrationElementOutput.add(NarratedElementType.TITLE, name);
                }
            });
        }

        protected void refreshEntry() { }
    }

    @Environment(EnvType.CLIENT)
    public class KeyEntry extends Entry {
        private final KeyMapping key;
        private final Component name;
        private final Button changeButton;
        private final Button resetButton;
        private boolean hasCollision = false;

        KeyEntry(ConfigList keybindsList, KeyMapping key, Component name) {
            this.key = key;
            this.name = name;
            this.changeButton = Button.builder(name, (button) -> {
                keybindsList.configScreen.selectedKey = key;
                keybindsList.resetMappingAndUpdateButtons();
            }).bounds(0, 0, 75, 20).createNarration((supplier) -> key.isUnbound() ? Component.translatable("narrator.controls.unbound", name) : Component.translatable("narrator.controls.bound", name, supplier.get())).build();
            this.resetButton = Button.builder(Component.translatable("controls.reset"), (e) -> {
                keybindsList.configScreen.manager.setKey(key, key.getDefaultKey());
                keybindsList.resetMappingAndUpdateButtons();
            }).bounds(0, 0, 50, 20).createNarration((e) -> Component.translatable("narrator.controls.reset", name)).build();
            this.refreshEntry();
        }

        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            int var10003 = left + 90 - ConfigList.this.maxNameWidth;
            int var10004 = top + height / 2;
            guiGraphics.drawString(minecraft.font, this.name, var10003, var10004 - 9 / 2, 16777215, false);
            this.resetButton.setX(left + 190);
            this.resetButton.setY(top);
            this.resetButton.render(guiGraphics, mouseX, mouseY, partialTick);
            this.changeButton.setX(left + 105);
            this.changeButton.setY(top);
            if (this.hasCollision) {
                int j = this.changeButton.getX() - 6;
                guiGraphics.fill(j, top + 2, j + 3, top + height + 2, ChatFormatting.RED.getColor() | -16777216);
            }

            this.changeButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        protected void refreshEntry() {
            this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
            this.resetButton.active = !this.key.isDefault();
            this.hasCollision = false;
            MutableComponent mutableComponent = Component.empty();
            if (!this.key.isUnbound()) {
                for(KeyMapping keyMapping : ConfigList.this.configScreen.manager.unregisteredKeys()) {
                    if (keyMapping != this.key && this.key.same(keyMapping)) {
                        if (this.hasCollision) mutableComponent.append(", ");
                        this.hasCollision = true;
                        mutableComponent.append(Component.translatable(keyMapping.getName()));
                    }
                }
            }

            if (this.hasCollision) {
                this.changeButton.setMessage(Component.literal("[ ").append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.RED));
                this.changeButton.setTooltip(Tooltip.create(Component.translatable("controls.keybinds.duplicateKeybinds", mutableComponent)));
            } else this.changeButton.setTooltip(null);

            if (ConfigList.this.configScreen.selectedKey == this.key) this.changeButton.setMessage(Component.literal("> ").append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE)).append(" <").withStyle(ChatFormatting.YELLOW));
        }
    }
}
