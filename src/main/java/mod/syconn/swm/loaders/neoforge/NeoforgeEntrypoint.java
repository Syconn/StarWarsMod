//? if neoforge {
/*package mod.syconn.swm.loaders.fabric;
package mod.syconn.swm.loaders.neoforge;

import com.example.mymod.impl.registry.ModItems;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD)
public class NeoforgeEntrypoint {

  public NeoforgeEntrypoint(IEventBus modEventBus) {
    bind(modEventBus, Registries.ITEM, ModItems::register);
  }

  public <T> void bind(IEventBus modEventBus, ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
    modEventBus.addListener((Consumer<RegisterEvent>) event -> {
      if (registry.equals(event.getRegistryKey())) {
        source.accept((t, id) -> event.register(registry, id, () -> t));
      }
    });
  }
}
*///?}
