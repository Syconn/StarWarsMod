package mod.syconn.swm.mixin;

import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.clientside.PreciseEntityVelocityUpdatePacket;
import mod.syconn.swm.utils.interfaces.IPrecisionVelocityEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class EntityTrackerEntryMixin {
    @Final
    @Shadow
    private Entity entity;

    @Inject(method = "sendChanges", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/server/level/ServerEntity;tickCount:I"), cancellable = true)
    void tick(CallbackInfo ci) {
        if (!(entity instanceof IPrecisionVelocityEntity)) return;

        if (this.entity.hurtMarked) {
            if (this.entity instanceof ServerPlayer sp) Network.CHANNEL.sendToPlayer(sp, new PreciseEntityVelocityUpdatePacket(this.entity));
            this.entity.hurtMarked = false;
        }
        ci.cancel();
    }

    @Shadow
    protected abstract void broadcastAndSend(Packet<?> packet);
}
