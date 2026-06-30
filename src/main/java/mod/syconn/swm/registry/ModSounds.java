package mod.syconn.swm.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class ModSounds { // TODO FIX DEFLECTION, DUAL WEILD NOT DUAL WIELDABLE GUNS

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Constants.MOD, Registries.SOUND_EVENT);

    static {
        register("item.blaster.bowcaster");
        register("item.blaster.cycler");
        register("item.blaster.dc15");
        register("item.blaster.dc17");
        register("item.blaster.ion");
    }

    public static final RegistrySupplier<SoundEvent> LIGHTSABER_ACTIVATION = register("item.lightsaber.activation");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_DEACTIVATION = register("item.lightsaber.deactivation");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_CLASH = register("item.lightsaber.clash");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_SWING = register("item.lightsaber.swing");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_IDLE = register("item.lightsaber.idle");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_DEFLECT = register("item.lightsaber.deflect");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_DEFLECT2 = register("item.lightsaber.deflect2");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_IMPACT = register("item.lightsaber.impact");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_IMPACT2 = register("item.lightsaber.impact2");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_RETURN = register("item.lightsaber.return");
    public static final RegistrySupplier<SoundEvent> LIGHTSABER_THROWN = register("item.lightsaber.thrown");

    public static final RegistrySupplier<SoundEvent> COOLING = register("item.blaster.cooling");
    public static final RegistrySupplier<SoundEvent> HISS = register("item.blaster.hiss");
    public static final RegistrySupplier<SoundEvent> OVERHEAT = register("item.blaster.overheat");
    public static final RegistrySupplier<SoundEvent> STUN = register("item.blaster.stun");
    public static final RegistrySupplier<SoundEvent> VENT = register("item.blaster.vent");
    public static final RegistrySupplier<SoundEvent> FAILED = register("item.blaster.failed");
    public static final RegistrySupplier<SoundEvent> PRIMARY_COOL = register("item.blaster.primary");
    public static final RegistrySupplier<SoundEvent> SECONDARY_COOL = register("item.blaster.secondary");
    public static final RegistrySupplier<SoundEvent> SHOOT_FALLBACK = register("item.blaster.e11");

    public static final RegistrySupplier<SoundEvent> HOLOGRAM_ACTIVATE = register("block.hologram.activate");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_DEACTIVATE = register("block.hologram.deactivate");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_STATIC = register("block.hologram.static");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_BUTTON1 = register("block.hologram.button1");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_BUTTON2 = register("block.hologram.button2");
    public static final RegistrySupplier<SoundEvent> HOLOGRAM_BUTTON3 = register("block.hologram.button3");

    private static RegistrySupplier<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> SoundEvent.createVariableRangeEvent(Constants.withId(key)));
    }

    public static SoundEvent getOrDefault(String sound, SoundEvent fallback) {
        return BuiltInRegistries.SOUND_EVENT.getOptional(Constants.withId(sound)).orElse(fallback);
    }
}
