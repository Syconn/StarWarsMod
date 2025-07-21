package mod.syconn.swm.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Constants.MOD, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> LIGHTSABER_ACTIVATION = register("item.lightsaber.activation");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_DEACTIVATION = register("item.lightsaber.deactivation");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_CLASH = register("item.lightsaber.clash");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_SWING = register("item.lightsaber.swing");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_AMBIENT = register("item.lightsaber.ambient");

    private static RegistrySupplier<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Constants.MOD, key)));
    }
}
