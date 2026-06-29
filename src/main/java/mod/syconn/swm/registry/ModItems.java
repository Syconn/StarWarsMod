package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.RegistryEntry;
import net.minecraft.world.item.Item;

@AutoRegister
public class ModItems {

    public static final RegistryEntry<Item> MAGIC_STICK = RegistryEntry.item("magic_stick", () -> new Item(new Item.Properties()));

//    public static final Item
}
