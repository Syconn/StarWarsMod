package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.fabric.client.data.LightsaberDefaults;
import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LightsaberDataProvider implements DataProvider {

    private final FabricDataGenerator output;
    public final Map<String, LightsaberData> lightsaberData = new HashMap<>();

    public LightsaberDataProvider(FabricDataGenerator output) {
        this.output = output;
    }

    private void generateData() {
        for (var type : LightsaberDefaults.LightsaberTypes.values()) lightsaberData.put(type.getId(), type.getData());
    }

    public void run(@NotNull CachedOutput output) throws IOException {
        lightsaberData.clear();
        generateData();
        for (var data : this.lightsaberData.entrySet()) {
            var path = getPath(data.getKey());
            DataProvider.saveStable(output, data.getValue().toJson(), path);
        }
    }

    private Path getPath(String id) {
        return this.output.getOutputFolder(DataGenerator.Target.DATA_PACK).resolve(Constants.MOD).resolve("lightsaber/defaults").resolve(id + ".json");
    }

    public @NotNull String getName() {
        return "Lightsabers";
    }
}
