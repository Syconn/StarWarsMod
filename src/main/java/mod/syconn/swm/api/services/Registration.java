package mod.syconn.swm.api.services;

//? if fabric && >1.21.11
//import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

//? if fabric && <1.21.11
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Registration {

    public static CreativeModeTab.Builder createCreativeModeTabBuilder() {
        //? if !fabric
        //return CreativeModeTab.builder();
        //? if fabric && <1.21.11
        return FabricItemGroup.builder();
        //? if fabric && >1.21.11
        //return FabricCreativeModeTab.builder();
    }

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> function, Supplier<Block[]> validBlocksSupplier) {
        return BlockEntityType.Builder.of(function::apply, validBlocksSupplier.get()).build(null);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> function) {
        return new MenuType<>(function::apply, FeatureFlags.DEFAULT_FLAGS);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(TriFunction<Integer, Inventory, FriendlyByteBuf, T> function) {
        return new ExtendedScreenHandlerType<>(function::apply);
    }
}
