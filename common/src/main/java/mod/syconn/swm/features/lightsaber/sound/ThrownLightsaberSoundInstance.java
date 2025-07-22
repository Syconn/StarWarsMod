package mod.syconn.swm.features.lightsaber.sound;

import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaberEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class ThrownLightsaberSoundInstance extends AbstractTickableSoundInstance {

    private final ThrownLightsaberEntity thrownLightsaber;

    public ThrownLightsaberSoundInstance(ThrownLightsaberEntity thrownLightsaber) {
        super(ModSounds.LIGHTSABER_THROWN.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.thrownLightsaber = thrownLightsaber;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1F;
        this.x = thrownLightsaber.getX();
        this.y = thrownLightsaber.getY();
        this.z = thrownLightsaber.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.thrownLightsaber.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.thrownLightsaber.isRemoved()) {
            this.stop();
            return;
        }

        this.x = this.thrownLightsaber.getX();
        this.y = this.thrownLightsaber.getY();
        this.z = this.thrownLightsaber.getZ();
    }
}
