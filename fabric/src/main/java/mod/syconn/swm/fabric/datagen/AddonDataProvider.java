package mod.syconn.swm.fabric.datagen;

import mod.syconn.swm.fabric.client.data.BlasterDefaults;
import mod.syconn.swm.fabric.client.data.LightsaberDefaults;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.interfaces.ISerializable;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AddonDataProvider implements DataProvider {

    private final FabricDataOutput output;
    public final Map<String, Map<String, ISerializable<CompoundTag>>> addonData = new HashMap<>();

    public AddonDataProvider(FabricDataOutput output) {
        this.output = output;
    }

    private void generateData() {
        addonData.put("lightsaber", Arrays.stream(LightsaberDefaults.LightsaberTypes.values()).collect(Collectors.toMap(LightsaberDefaults.LightsaberTypes::getId, LightsaberDefaults.LightsaberTypes::getData)));
        addonData.put("blaster", Arrays.stream(BlasterDefaults.BlasterTypes.values()).collect(Collectors.toMap(BlasterDefaults.BlasterTypes::getId, BlasterDefaults.BlasterTypes::getData)));
    }

    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        addonData.clear();
        generateData();
        return generateAll(output);
    }

    protected CompletableFuture<?> generateAll(CachedOutput cache) {
        ArrayList<CompletableFuture<?>> futures = new ArrayList<>();

        for (var outer : this.addonData.entrySet()) {
            for (var data : outer.getValue().entrySet()) {
                var path = getPath(outer.getKey(), data.getKey());
                futures.add(DataProvider.saveStable(cache, data.getValue().toJson(), path));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private Path getPath(String path, String id) {
        return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Constants.MOD).resolve("%s/defaults".formatted(path)).resolve(id + ".json");
    }

    public @NotNull String getName() {
        return "Addon Data";
    }
}
