package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

@AutoRegister
public class ModSounds { // TODO FIX DEFLECTION, DUAL WEILD NOT DUAL WIELDABLE GUNS

    public static final RegistryEntry<SoundEvent> LIGHTSABER_ACTIVATION = RegistryEntry.soundEvent("item.lightsaber.activation");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_DEACTIVATION = RegistryEntry.soundEvent("item.lightsaber.deactivation");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_CLASH = RegistryEntry.soundEvent("item.lightsaber.clash");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_SWING = RegistryEntry.soundEvent("item.lightsaber.swing");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_IDLE = RegistryEntry.soundEvent("item.lightsaber.idle");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_DEFLECT = RegistryEntry.soundEvent("item.lightsaber.deflect");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_DEFLECT2 = RegistryEntry.soundEvent("item.lightsaber.deflect2");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_IMPACT = RegistryEntry.soundEvent("item.lightsaber.impact");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_IMPACT2 = RegistryEntry.soundEvent("item.lightsaber.impact2");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_RETURN = RegistryEntry.soundEvent("item.lightsaber.return");
    public static final RegistryEntry<SoundEvent> LIGHTSABER_THROWN = RegistryEntry.soundEvent("item.lightsaber.thrown");

    public static final RegistryEntry<SoundEvent> COOLING = RegistryEntry.soundEvent("item.blaster.cooling");
    public static final RegistryEntry<SoundEvent> HISS = RegistryEntry.soundEvent("item.blaster.hiss");
    public static final RegistryEntry<SoundEvent> OVERHEAT = RegistryEntry.soundEvent("item.blaster.overheat");
    public static final RegistryEntry<SoundEvent> STUN = RegistryEntry.soundEvent("item.blaster.stun");
    public static final RegistryEntry<SoundEvent> VENT = RegistryEntry.soundEvent("item.blaster.vent");
    public static final RegistryEntry<SoundEvent> FAILED = RegistryEntry.soundEvent("item.blaster.failed");
    public static final RegistryEntry<SoundEvent> PRIMARY_COOL = RegistryEntry.soundEvent("item.blaster.primary");
    public static final RegistryEntry<SoundEvent> SECONDARY_COOL = RegistryEntry.soundEvent("item.blaster.secondary");
    public static final RegistryEntry<SoundEvent> SHOOT_FALLBACK = RegistryEntry.soundEvent("item.blaster.e11");

    public static final RegistryEntry<SoundEvent> SHOOT_BOWCASTER = RegistryEntry.soundEvent("item.blaster.bowcaster");
    public static final RegistryEntry<SoundEvent> SHOOT_CYCLER = RegistryEntry.soundEvent("item.blaster.cycler");
    public static final RegistryEntry<SoundEvent> SHOOT_DC15 = RegistryEntry.soundEvent("item.blaster.dc15");
    public static final RegistryEntry<SoundEvent> SHOOT_DC17 = RegistryEntry.soundEvent("item.blaster.dc17");
    public static final RegistryEntry<SoundEvent> SHOOT_ION = RegistryEntry.soundEvent("item.blaster.ion");

    public static final RegistryEntry<SoundEvent> HOLOGRAM_ACTIVATE = RegistryEntry.soundEvent("block.hologram.activate");
    public static final RegistryEntry<SoundEvent> HOLOGRAM_DEACTIVATE = RegistryEntry.soundEvent("block.hologram.deactivate");
    public static final RegistryEntry<SoundEvent> HOLOGRAM_STATIC = RegistryEntry.soundEvent("block.hologram.static");
    public static final RegistryEntry<SoundEvent> HOLOGRAM_BUTTON1 = RegistryEntry.soundEvent("block.hologram.button1");
    public static final RegistryEntry<SoundEvent> HOLOGRAM_BUTTON2 = RegistryEntry.soundEvent("block.hologram.button2");
    public static final RegistryEntry<SoundEvent> HOLOGRAM_BUTTON3 = RegistryEntry.soundEvent("block.hologram.button3");

    public static SoundEvent getOrDefault(String sound, SoundEvent fallback) {
        return BuiltInRegistries.SOUND_EVENT.getOptional(Constants.withId(sound)).orElse(fallback);
    }
}
