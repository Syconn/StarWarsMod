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
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_DEFLECT = register("item.lightsaber.deflect");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_DEFLECT2 = register("item.lightsaber.deflect2");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_IMPACT = register("item.lightsaber.impact");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_IMPACT2 = register("item.lightsaber.impact2");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_RETURN = register("item.lightsaber.return");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_THROWN = register("item.lightsaber.thrown");

    public static final RegistrySupplier<SoundEvent> HOLOGRAM_ACTIVATE = register("block.hologram.activate");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_DEACTIVATE = register("block.hologram.deactivate");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_STATIC = register("block.hologram.static");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_BUTTON1 = register("block.hologram.button1");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_BUTTON2 = register("block.hologram.button2");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_BUTTON3 = register("block.hologram.button3");

    private static RegistrySupplier<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Constants.MOD, key)));
    }
}
