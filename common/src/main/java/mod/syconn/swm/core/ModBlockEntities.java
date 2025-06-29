package mod.syconn.swm.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Constants.MOD, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<LightsaberWorkbenchBlockEntity>> LIGHTSABER_WORKBENCH = BLOCK_ENTITIES.register("lightsaber_workbench",
            () -> BlockEntityType.Builder.of(LightsaberWorkbenchBlockEntity::new, ModBlocks.LIGHTSABER_WORKBENCH.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<HoloProjectorBlockEntity>> HOLO_PROJECTOR = BLOCK_ENTITIES.register("holo_projector",
            () -> BlockEntityType.Builder.of(HoloProjectorBlockEntity::new, ModBlocks.HOLO_PROJECTOR.get()).build(null));
}
