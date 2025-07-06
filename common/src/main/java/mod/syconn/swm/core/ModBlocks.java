package mod.syconn.swm.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.block.HoloProjectorBlock;
import mod.syconn.swm.features.lightsaber.block.LightsaberWorkbenchBlock;
import mod.syconn.swm.item.HoloProjectorItem;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD, Registries.BLOCK);

    public static final RegistrySupplier<LightsaberWorkbenchBlock> LIGHTSABER_WORKBENCH = register("lightsaber_workbench", LightsaberWorkbenchBlock::new);
    public static final RegistrySupplier<HoloProjectorBlock> HOLO_PROJECTOR = register("holo_projector", HoloProjectorBlock::new, HoloProjectorItem::new);

    private static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> supplier) {
        return register(id, supplier, BlockItem::new);
    }

    private static <B extends Block, I extends BlockItem> RegistrySupplier<B> register(String id, Supplier<B> blockSupplier, BiFunction<B, Item.Properties, I> itemSupplier) {
        RegistrySupplier<B> registeredBlock = BLOCKS.register(id, blockSupplier);
        ModItems.ITEMS.register(id, () -> itemSupplier.apply(registeredBlock.get(), new Item.Properties().arch$tab(ModItems.TAB)));
        return registeredBlock;
    }
}
