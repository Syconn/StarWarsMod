package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.sound.LightsaberAudio;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class PlayAmbientLightsaberSoundPacket {

    private final int entityId;
    private final EquipmentSlot equipmentSlot;

    public PlayAmbientLightsaberSoundPacket(int entityId, EquipmentSlot equipmentSlot) {
        this.entityId = entityId;
        this.equipmentSlot = equipmentSlot;
    }

    public PlayAmbientLightsaberSoundPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readEnum(EquipmentSlot.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeEnum(this.equipmentSlot);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() != null) {
                var entity = context.get().getPlayer().level().getEntity(this.entityId);
                if (entity instanceof LivingEntity le) LightsaberAudio.playAmbientLightsaber(le, this.equipmentSlot);
            }
        });
    }
}
