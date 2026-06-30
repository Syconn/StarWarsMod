package mod.syconn.swm.features.lightsaber.client.sound;

import mod.syconn.swm.registry.ModSounds;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.server.sound.DopplerSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class IdleLightsaberSoundInstance extends DopplerSoundInstance {

    private final Player player;

    public IdleLightsaberSoundInstance(Player player) {
        super(player, ModSounds.LIGHTSABER_IDLE.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.7F;
        this.x = (float)player.getX();
        this.y = (float)player.getY();
        this.z = (float)player.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.player.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.player.isRemoved()) {
            this.stop();
            return;
        }

        var foundSaber = false;

        if (player.getMainHandItem().getItem() instanceof LightsaberItem) foundSaber = tryUseStack(player.getMainHandItem());

        if (!foundSaber && player.getOffhandItem().getItem() instanceof LightsaberItem) foundSaber = tryUseStack(player.getOffhandItem());

        if (!foundSaber) {
            this.stop();
            return;
        }

        this.x = (float)this.player.getX();
        this.y = (float)this.player.getY();
        this.z = (float)this.player.getZ();
    }

    private boolean tryUseStack(ItemStack stack) {
        var lt = LightsaberTag.getOrCreate(stack);

        var size = lt.getSize();
        if (size > 0) {
            volume = size;
            return true;
        }

        return false;
    }

    public static boolean areConditionsMet(Player player) {
        return isActiveLightsaber(player.getMainHandItem()) || isActiveLightsaber(player.getOffhandItem());
    }

    private static boolean isActiveLightsaber(ItemStack stack) {
        if (!(stack.getItem() instanceof LightsaberItem)) return false;

        var lt = LightsaberTag.getOrCreate(stack);
        return lt.getSize() > 0;
    }
}
