package mod.syconn.swm.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.features.lightsaber.block.LightsaberWorkbenchBlock;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD, Registries.BLOCK);

    public static final RegistrySupplier<LightsaberWorkbenchBlock> LIGHTSABER_WORKBENCH = register("lightsaber_workbench", LightsaberWorkbenchBlock::new);

    private static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> supplier) {
        RegistrySupplier<T> registeredBlock = BLOCKS.register(id, supplier);
        ModItems.ITEMS.register(id, () -> new BlockItem(registeredBlock.get(), new Item.Properties().arch$tab(ModItems.TAB)));
        return registeredBlock;
    }
}
