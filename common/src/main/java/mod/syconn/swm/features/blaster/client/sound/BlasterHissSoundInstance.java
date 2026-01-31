package mod.syconn.swm.features.blaster.client.sound;

import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.blaster.entity.BlasterBoltEntity;
import mod.syconn.swm.server.sound.DopplerSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class BlasterHissSoundInstance extends DopplerSoundInstance {

    public BlasterHissSoundInstance(BlasterBoltEntity entity) {
        super(entity, ModSounds.HISS.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
    }

    @Override
    public boolean canPlaySound() {
        return !this.source.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.source.isRemoved()) {
            this.stop();
            return;
        }

        this.volume = 1;
        this.x = (float)this.source.getX();
        this.y = (float)this.source.getY();
        this.z = (float)this.source.getZ();
    }
}
