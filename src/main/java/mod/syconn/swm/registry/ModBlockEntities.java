package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@AutoRegister
public class ModBlockEntities {

    public static final RegistryEntry<BlockEntityType<LightsaberWorkbenchBlockEntity>> LIGHTSABER_WORKBENCH = RegistryEntry.blockEntity("lightsaber_workbench", LightsaberWorkbenchBlockEntity::new, ModBlocks.LIGHTSABER_WORKBENCH::get);
}
