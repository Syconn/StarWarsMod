package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.core.ModBlocks;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.core.ModKeys;
import mod.syconn.swm.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import static mod.syconn.swm.util.Constants.MOD;

public class LangProvider extends FabricLanguageProvider {

    public LangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup." + MOD + ".star_wars", "Syconn's Star Wars");

        translationBuilder.add("key.categories." + Constants.MOD, "Star Wars Controls");
        translationBuilder.add("key.swm.toggle_item", "Toggle Held Item");
        translationBuilder.add("key.swm.power_1", "Use Force Power 1");

        translationBuilder.add(ModItems.LIGHTSABER.get(), "Lightsaber");
        translationBuilder.add(ModItems.MONITOR.get(), "Monitor");
        translationBuilder.add(ModItems.DRIVER.get(), "Screw Driver");
        translationBuilder.add(ModItems.DRILL.get(), "Drill");
        translationBuilder.add(ModItems.SCREEN.get(), "Screen");

        translationBuilder.add(ModBlocks.LIGHTSABER_WORKBENCH.get(), "Lightsaber Workbench");
    }
}
