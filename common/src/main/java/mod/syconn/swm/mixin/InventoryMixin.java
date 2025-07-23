package mod.syconn.swm.mixin;

import mod.syconn.swm.server.data.SWGear;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow
    @Final
    public Player player;

    @Inject(method = "tick", at = @At("RETURN"))
    private void updateItems(CallbackInfo ci) {
        ((SWGear.SWGearAccess) this.player).swm$getSWGear().tick();
    }
}
