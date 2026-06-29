package mod.syconn.swm.api.services;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;

public class RegistrationService {

    public static CreativeModeTab.Builder createCreativeModeTabBuilder() {
        //? if !fabric && !neoforge
        //return CreativeModeTab.builder();
        //? if fabric
        return FabricItemGroup.builder();
    }
}
