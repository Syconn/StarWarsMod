package mod.syconn.swm.server.sound;

import mod.syconn.swm.utils.generic.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class DopplerSoundInstance extends AbstractTickableSoundInstance {

    protected final Entity source;

    protected DopplerSoundInstance(Entity source, SoundEvent soundEvent, SoundSource soundSource, RandomSource random) {
        super(soundEvent, soundSource, random);
        this.source = source;
    }

    @Override
    public void tick() {
        var mc = Minecraft.getInstance();
        if (mc.getCameraEntity() != null) this.pitch = 1 + MathUtil.calculateDopplerShift(source, mc.getCameraEntity());
    }
}
