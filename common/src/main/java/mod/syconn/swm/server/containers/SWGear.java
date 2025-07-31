package mod.syconn.swm.server.containers;

import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.serverside.SetEquipmentSlotPacket;
import mod.syconn.swm.server.containers.slot.EquipmentItemSlot;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SWGear implements Container {

    private final NonNullList<ItemStack> gear = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
//    private final Inventory inventory;
//
//    public SWGear(Inventory inventory) {
//        this.inventory = inventory;
//    }

    public @NotNull ItemStack getItemFromSlot(IEquipmentItem.SWEquipmentSlot slot) {
        return getItem(slot.getSlot());
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
        var itemStack = ContainerHelper.removeItem(this.gear, slot.getSlot(), getItemFromSlot(slot).getCount());
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
        setItem(slot.getSlot(), stack);
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
    public void setChanged() {
//        System.out.println(this.inventory);
    }

    public CompoundTag save(){
        var tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, this.gear);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.gear.clear();
        ContainerHelper.loadAllItems(tag, this.gear);
    }

    public static void getAndSyncGear(ServerPlayer serverPlayer) {
        for (var i = 0; i < serverPlayer.swm$getSWGear().gear.size(); i++)
            Network.CHANNEL.sendToPlayers(serverPlayer.serverLevel().players(), new SetEquipmentSlotPacket(serverPlayer.getUUID(), serverPlayer.swm$getSWGear().gear.get(i), IEquipmentItem.SWEquipmentSlot.getSlot(i)));
        for (var player : serverPlayer.serverLevel().players())
            for (var i = 0; i < player.swm$getSWGear().gear.size(); i++)
                Network.CHANNEL.sendToPlayer(serverPlayer, new SetEquipmentSlotPacket(player.getUUID(), player.swm$getSWGear().gear.get(i), IEquipmentItem.SWEquipmentSlot.getSlot(i)));
    }

    public interface SWGearAccess {
        default SWGear swm$getSWGear() {
            return new SWGear();
        }
    }
}