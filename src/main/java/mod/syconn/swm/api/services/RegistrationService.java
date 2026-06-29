package mod.syconn.swm.api.services;

//? if fabric && >1.21.11
//import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

//? if fabric && <1.21.11
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.world.item.CreativeModeTab;

public class RegistrationService {

    public static CreativeModeTab.Builder createCreativeModeTabBuilder() {
        //? if !fabric
        //return CreativeModeTab.builder();
        //? if fabric && <1.21.11
        return FabricItemGroup.builder();
        //? if fabric && >1.21.11
        //return FabricCreativeModeTab.builder();
    }
}
