package mod.syconn.swm.registry;

import mod.syconn.swm.api.registry.AutoRegister;
import mod.syconn.swm.api.registry.RegistryEntry;
import mod.syconn.swm.features.lightsaber.block.LightsaberWorkbenchBlock;

@AutoRegister
public class ModBlocks {

    public static final RegistryEntry<LightsaberWorkbenchBlock> LIGHTSABER_WORKBENCH = RegistryEntry.blockWithItem("lightsaber_workbench", LightsaberWorkbenchBlock::new);
}
