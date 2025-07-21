package mod.syconn.swm.features.lightsaber.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import static mod.syconn.swm.core.ModSounds.LIGHTSABER_ACTIVATION;
import static mod.syconn.swm.core.ModSounds.LIGHTSABER_DEACTIVATION;

public class LightsaberAudio {

    public static void playToggleAudio(Level level, BlockPos pos, boolean active) {
        level.playSound(null, pos, active ? LIGHTSABER_ACTIVATION.get() : LIGHTSABER_DEACTIVATION.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
    }
}
