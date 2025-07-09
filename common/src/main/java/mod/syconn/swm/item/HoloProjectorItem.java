package mod.syconn.swm.item;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.block.HoloProjectorBlock;
import mod.syconn.swm.client.ClientHooks;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.client.HologramData;
import mod.syconn.swm.utils.interfaces.IItemExtensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HoloProjectorItem extends BlockItem implements IItemExtensions {

    public HoloProjectorItem(HoloProjectorBlock block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (level.isClientSide) {
            EnvExecutor.runInEnv(Env.CLIENT, () -> () -> GameInstance.getClient().setScreen(ClientHooks.createHologramScreen(null, stack)));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack from, @NotNull ItemStack to, boolean changed) {
        return changed;
    }

    @Override // TODO TEST WITH PLAYER IN RENDERER
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        var id = HologramData.HologramTag.getOrCreate(stack);
        if (level instanceof ServerLevel serverLevel) {
            var network = HologramNetwork.get(serverLevel);
            var callId = network.getCallId(id.itemId);
            if (callId != null && network.getCall(callId) != null) {
                var call = network.getCall(callId);
                var playerList = call.participants().values().stream().filter(p -> p.item() != null).findFirst();
                if (playerList.isPresent()) {
                    var caller = call.owner().uuid().equals(entity.getUUID()) ? playerList.get() : call.owner();
                    if (serverLevel.getPlayerByUUID(caller.uuid()) == null && id.uuid != null) HologramData.HologramTag.update(stack, null);
                    else if (!caller.uuid().equals(id.uuid)) HologramData.HologramTag.update(stack, caller.uuid());
                    return;
                }
            }
            if (id.uuid != null) HologramData.HologramTag.update(stack, null);
        }
    }
}
