package mod.syconn.swm.mixin.client;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaberEntity;
import mod.syconn.swm.features.lightsaber.client.sound.ThrownLightsaberSoundInstance;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "postAddEntitySoundInstance", at = @At(value = "TAIL"))
    public void addEntitySoundInstance(Entity entity, CallbackInfo ci) {
        if (entity instanceof ThrownLightsaberEntity thrownLightsaber) {
            GameInstance.getClient().getSoundManager().play(new ThrownLightsaberSoundInstance(thrownLightsaber));
        }
    }
}
