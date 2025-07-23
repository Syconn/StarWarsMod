package mod.syconn.swm.server.containers.slot;

import com.mojang.datafixers.util.Pair;
import mod.syconn.swm.server.data.SWGear;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EquipmentItemSlot extends Slot {

    private final Player player;
    private final IEquipmentItem.SWEquipmentSlot slot;

    public EquipmentItemSlot(Player player, IEquipmentItem.SWEquipmentSlot slot, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.player = player;
        this.slot = slot;
    }

    public void setChanged() {
        player.getInventory().setChanged();
//        SWGear.getOrCreate(player).change(player); TODO DO I NEED
        super.setChanged();
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return super.getNoItemIcon();
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof IEquipmentItem && ((IEquipmentItem) stack.getItem()).getSWEquipmentSlot() == slot;
    }
}
