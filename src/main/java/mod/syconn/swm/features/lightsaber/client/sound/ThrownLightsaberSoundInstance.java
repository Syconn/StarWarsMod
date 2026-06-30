package mod.syconn.swm.features.lightsaber.client.sound;

import mod.syconn.swm.registry.ModSounds;
import mod.syconn.swm.features.lightsaber.entity.ThrownLightsaberEntity;
import mod.syconn.swm.server.sound.DopplerSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

@Environment(EnvType.CLIENT)
public class ThrownLightsaberSoundInstance extends DopplerSoundInstance {

    public ThrownLightsaberSoundInstance(ThrownLightsaberEntity entity) {
        super(entity, ModSounds.LIGHTSABER_THROWN.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = 0;
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

        this.volume = (float)((Math.sin(source.tickCount) + 1) / 2f);

        this.x = (float)this.source.getX();
        this.y = (float)this.source.getY();
        this.z = (float)this.source.getZ();
    }
}
