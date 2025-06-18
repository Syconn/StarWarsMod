package mod.syconn.swm.forge.client.datagen;

import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.forge.client.data.LightsaberDefaults;
import mod.syconn.swm.util.Constants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LightsaberDataProvider implements DataProvider {

    private final PackOutput output;
    public final Map<String, LightsaberData> lightsaberData = new HashMap<>();

    public LightsaberDataProvider(PackOutput output) {
        this.output = output;
    }

    private void generateData() {
        for (var type : LightsaberDefaults.LightsaberTypes.values()) lightsaberData.put(type.getId(), type.getData());
    }

    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        lightsaberData.clear();
        generateData();
        return generateAll(output);
    }

    protected CompletableFuture<?> generateAll(CachedOutput cache) {
        CompletableFuture<?>[] futures = new CompletableFuture<?>[this.lightsaberData.size()];

        var i = 0;
        for (var data : this.lightsaberData.entrySet()) {
            var path = getPath(data.getKey());
            futures[i++] = DataProvider.saveStable(cache, data.getValue().toJson(), path);
        }

        return CompletableFuture.allOf(futures);
    }

    private Path getPath(String id) {
        return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Constants.MOD).resolve("lightsaber/defaults").resolve(id + ".json");
    }

    public @NotNull String getName() {
        return "Lightsabers";
    }
}
