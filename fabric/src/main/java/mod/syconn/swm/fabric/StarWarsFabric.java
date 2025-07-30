package mod.syconn.swm.fabric;

import mod.syconn.swm.StarWars;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.mixin.itemgroup.client.CreativeInventoryScreenMixin;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

public final class StarWarsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StarWars.init();
    }
}
