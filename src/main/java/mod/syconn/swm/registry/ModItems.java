package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.api.registry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static mod.syconn.swm.utils.Constants.MOD;

@AutoRegister
public class ModItems {

    public static final RegistryEntry<Item> MAGIC_STICK = RegistryEntry.item("magic_stick", prop -> new Item(prop)).setTabbed(true);

    public static final RegistryEntry<CreativeModeTab> TAB = RegistryEntry.creativeModeTab("star_wars", b -> b.title(Component.translatable("itemGroup." + MOD + ".starwars")).icon(() -> new ItemStack(MAGIC_STICK.get())).displayItems(Registrar::addCreative));
}
