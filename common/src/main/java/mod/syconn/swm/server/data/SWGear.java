package mod.syconn.swm.server.data;

import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SWGear implements Container {

    private final Player player;
    private final NonNullList<ItemStack> gear = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);;

    public SWGear(Player player) {
        this.player = player;
    }

    public void tick() {
        for (int slot = 0; slot < this.getContainerSize(); slot++) this.getItem(slot).inventoryTick(this.player.level(), this.player, -1, false);
    }

    private int slotFromEquipment(IEquipmentItem.SWEquipmentSlot slot) {
        if (slot == IEquipmentItem.SWEquipmentSlot.LIGHTSABER) return 0;
        return -1;
    }

    public @NotNull ItemStack getItemFromSlot(IEquipmentItem.SWEquipmentSlot slot) {
        if (slotFromEquipment(slot) != -1) return getItem(slotFromEquipment(slot));
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.gear.size() ? this.gear.get(slot) : ItemStack.EMPTY;
    }

    public @NotNull ItemStack clearSlot(int slot) {
        var itemStack = ContainerHelper.removeItem(this.gear, slot, getItem(slot).getCount());
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        var itemStack = ContainerHelper.removeItem(this.gear, slot, amount);
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        var itemStack = this.gear.get(slot);
        if (itemStack.isEmpty()) return ItemStack.EMPTY;
        else {
            this.gear.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    public @NotNull ItemStack removeItemNoUpdate(IEquipmentItem.SWEquipmentSlot slot) {
        var itemStack = ContainerHelper.removeItem(this.gear, slotFromEquipment(slot), getItemFromSlot(slot).getCount());
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.gear.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) stack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    public void setItem(IEquipmentItem.SWEquipmentSlot slot, ItemStack stack) {
        setItem(slotFromEquipment(slot), stack);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (var itemStack : this.gear) if (!itemStack.isEmpty()) return false;
        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.gear.clear();
        this.setChanged();
    }

    @Override
    public void setChanged() {}

    public CompoundTag save(){
        var tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, this.gear);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.gear.clear();
        ContainerHelper.loadAllItems(tag, this.gear);
    }

    public interface SWGearAccess {
        SWGear swm$getSWGear();
    }
}
