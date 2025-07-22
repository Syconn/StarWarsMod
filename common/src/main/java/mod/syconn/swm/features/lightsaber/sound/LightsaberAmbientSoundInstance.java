package mod.syconn.swm.features.lightsaber.sound;

import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class LightsaberAmbientSoundInstance extends AbstractTickableSoundInstance {
    private final LivingEntity holder;
    private final EquipmentSlot slot;

    public LightsaberAmbientSoundInstance(LivingEntity holder, EquipmentSlot slot) {
        super(ModSounds.LIGHTSABER_AMBIENT.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.holder = holder;
        this.slot = slot;
        this.looping = false;
        this.delay = 0;
        this.volume = 0.1F;
        this.x = holder.getX();
        this.y = holder.getY();
        this.z = holder.getZ();
    }


    @Override
    public void tick() {
        var stack = this.holder.getItemBySlot(slot);
        if (stack.isEmpty() || !(stack.getItem() instanceof LightsaberItem) || !LightsaberTag.getOrCreate(stack).active || this.holder.isRemoved()) {
            this.stop();
            return;
        }

        this.x = this.holder.getX();
        this.y = this.holder.getY();
        this.z = this.holder.getZ();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LightsaberAmbientSoundInstance instance && instance.holder.equals(this.holder) && instance.slot.equals(this.slot);
    }
}
