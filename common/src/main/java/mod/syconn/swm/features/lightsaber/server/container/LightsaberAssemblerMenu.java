package mod.syconn.swm.features.lightsaber.server.container;

import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.core.ModMenus;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.server.containers.slot.SpecificSlot;
import mod.syconn.swm.server.recipes.LightsaberRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightsaberAssemblerMenu extends AbstractContainerMenu {

    private final LightsaberWorkbenchBlockEntity blockEntity;
    private final List<LightsaberRecipe> recipes;

    public LightsaberAssemblerMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        this(containerId, inventory, data.readBlockPos());
    }

    public LightsaberAssemblerMenu(int containerId, Inventory inventory, BlockPos pos){
        super(ModMenus.LIGHTSABER_ASSEMBLER.get(), containerId);

        this.blockEntity = inventory.player.level().getBlockEntity(pos, ModBlockEntities.LIGHTSABER_WORKBENCH.get()).orElseThrow();
        this.recipes = inventory.player.level().getRecipeManager().getAllRecipesFor(ModRecipes.LIGHTSABER.get());

        this.addSlot(new SpecificSlot(this.blockEntity.getContainer(), 0, 174, 37, LightsaberItem.class));
        for(int l = 0; l < 3; ++l) for(int j1 = 0; j1 < 9; ++j1) this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 102 + l * 18));
        for(int i1 = 0; i1 < 9; ++i1) this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 160));
    }

    public static MenuProvider menu(BlockPos pos) {
        return new MenuProvider() {
            public @NotNull Component getDisplayName() {
                return Component.literal("Lightsaber Workbench");
            }

            public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new LightsaberAssemblerMenu(i, inventory, pos);
            }
        };
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);
        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {
                if (!this.moveItemStackTo(rawStack, 1, 5, false)) {
                    if (quickMovedSlotIndex < 32) {
                        if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (rawStack.isEmpty()) {
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                quickMovedSlot.setChanged();
            }

            if (rawStack.getCount() == quickMovedStack.getCount()) {
                return ItemStack.EMPTY;
            }
            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

    public LightsaberWorkbenchBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public List<LightsaberRecipe> getRecipes() {
        return recipes;
    }
}
