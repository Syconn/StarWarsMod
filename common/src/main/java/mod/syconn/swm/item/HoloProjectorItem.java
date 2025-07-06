package mod.syconn.swm.item;

import mod.syconn.swm.block.HoloProjectorBlock;
import mod.syconn.swm.utils.client.HologramData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HoloProjectorItem extends BlockItem {

    public HoloProjectorItem(HoloProjectorBlock block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (level.isClientSide) return InteractionResultHolder.pass(stack);
        HologramData.HologramTag.update(stack, player.getUUID());
        return InteractionResultHolder.success(stack);
    }

//    @Override
//    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
//
//    }
}
