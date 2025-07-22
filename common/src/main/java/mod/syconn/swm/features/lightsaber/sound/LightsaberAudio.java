package mod.syconn.swm.features.lightsaber.sound;

import mod.syconn.swm.core.ModSounds;
import mod.syconn.swm.utils.generic.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import static mod.syconn.swm.core.ModSounds.LIGHTSABER_ACTIVATION;
import static mod.syconn.swm.core.ModSounds.LIGHTSABER_DEACTIVATION;

public class LightsaberAudio {

    public static void playToggleAudio(Level level, BlockPos pos, boolean active) {
        level.playSound(null, pos, active ? LIGHTSABER_ACTIVATION.get() : LIGHTSABER_DEACTIVATION.get(), SoundSource.PLAYERS, 0.25F, 1.0F);
    }

    public static void playDeflectAudio(Level level, BlockPos pos) {
        level.playSound(null, pos, MathUtil.randomChoice(ModSounds.LIGHTSABER_DEFLECT, ModSounds.LIGHTSABER_DEFLECT2).get(), SoundSource.PLAYERS, 0.25F, 1.0F);
    }

    public static void playImpactAudio(Level level, BlockPos pos) {
        level.playSound(null, pos, MathUtil.randomChoice(ModSounds.LIGHTSABER_IMPACT, ModSounds.LIGHTSABER_IMPACT2).get(), SoundSource.PLAYERS, 0.25F, 1.0F);
    }
}
