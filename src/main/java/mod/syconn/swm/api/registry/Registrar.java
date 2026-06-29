package mod.syconn.swm.api.registry;

//? if fabric {
import org.reflections.Reflections;
//? }

//? if !fabric && !neoforge {
/*import net.minecraftforge.fml.ModList;
*///? }

//? if neoforge {
/*import net.neoforged.fml.ModList;
*///? }

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class Registrar {

    private static final List<ResourceLocation> REGISTRATION_PRIORITY = Util.make(new LinkedList<>(), list -> {
        list.add(Registries.ATTRIBUTE.location());
        list.add(Registries.GAME_EVENT.location());
        list.add(Registries.SOUND_EVENT.location());
        list.add(Registries.FLUID.location());
        list.add(Registries.MOB_EFFECT.location());
        list.add(Registries.BLOCK.location());
        list.add(Registries.ENCHANTMENT.location());
        list.add(Registries.ENTITY_TYPE.location());
        list.add(Registries.ITEM.location());
        list.add(Registries.POTION.location());
        list.add(Registries.PARTICLE_TYPE.location());
        list.add(Registries.BLOCK_ENTITY_TYPE.location());
        list.add(Registries.CUSTOM_STAT.location());
        list.add(Registries.MENU.location());
        list.add(Registries.RECIPE_TYPE.location());
        list.add(Registries.RECIPE_SERIALIZER.location());
        list.add(Registries.COMMAND_ARGUMENT_TYPE.location());
    });

    private static final Map<ResourceLocation, List<RegistryEntry<?>>> ENTRIES = new HashMap<>();

    public static List<RegistryEntry<?>> get(ResourceKey<? extends Registry<?>> key) {
        return ENTRIES.getOrDefault(key.location(), Collections.emptyList());
    }

    public static void loadRegistries() {
        for (var clazz : getClasses()) {
            try {
                for (var field : clazz.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers())) continue;
                    if (!RegistryEntry.class.isAssignableFrom(field.getType())) continue;
                    var entry = (RegistryEntry<?>) field.get(null);
                    ENTRIES.computeIfAbsent(entry.getRegistry().location(), k -> new ArrayList<>()).add(entry);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed scanning " + clazz, e);
            }
        }
    }

    private static Set<Class<?>> getClasses() {
        //? if fabric {
        var reflections = new Reflections("mod.syconn.swm.registry");
        return reflections.getTypesAnnotatedWith(AutoRegister.class);
        //? }

        //? if !fabric {
        /*var annotation = ModList.get().getModContainerById("swm").orElseThrow(() -> new IllegalStateException("Couldn't find mod container")).getModInfo().getOwningFile().getFile().getScanResult();
        return annotation.getAnnotations().stream().filter(ann -> ann.annotationType().getClassName().equals(AutoRegister.class.getName()) && ann.clazz().getClassName().startsWith("mod.syconn.swm."))
                .map(ann -> {
                    try {
                        return Class.forName(ann.clazz().getClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Failed loading class: " + ann.clazz().getClassName(), e);
                    }
                }).collect(java.util.stream.Collectors.toSet());
        *///? }
    }

    public static List<RegistryEntry<?>> getEntries() {
        return ENTRIES.values().stream().flatMap(Collection::stream).sorted(Comparator.comparing(entry -> {
            int index = REGISTRATION_PRIORITY.indexOf(entry.getRegistry().location());
            return index != -1 ? index : 1000;
        })).collect(Collectors.toCollection(ArrayList::new));
    }

    public static void addCreative(CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output pOutput) {
        pOutput.acceptAll(Registrar.getItems());
    }

    public static Set<ItemStack> getItems() {
        return get(ResourceKey.createRegistryKey(BuiltInRegistries.ITEM.key().location())).stream().filter(entry -> entry.tabbed).map(entry -> new ItemStack((ItemLike) entry.get())).collect(Collectors.toSet());
    }
}
