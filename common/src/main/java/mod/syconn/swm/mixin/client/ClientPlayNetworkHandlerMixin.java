package mod.syconn.swm.mixin.client;

import mod.syconn.swm.network.packets.clientside.PreciseEntityVelocityUpdatePacket;
import mod.syconn.swm.utils.interfaces.IPrecisionVelocityEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
@Environment(EnvType.CLIENT)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    private ClientLevel level;

    @Inject(method = "handleSetEntityMotion", at = @At("TAIL"))
    private void onEntityVelocityUpdate(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
        Entity entity = this.level.getEntity(packet.getId());
        if (!(entity instanceof IPrecisionVelocityEntity ipe)) return;

        if (packet instanceof PreciseEntityVelocityUpdatePacket packet2) {
            entity.setPos(new Vec3(packet2.getPosition()));
            entity.setDeltaMovement(new Vec3(packet2.getVelocity()));
            ipe.onPrecisionVelocityPacket(packet2);
        }
    }
}
