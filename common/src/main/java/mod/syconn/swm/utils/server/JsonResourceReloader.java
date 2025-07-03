package mod.syconn.swm.utils.server;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.syconn.swm.utils.interfaces.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class JsonResourceReloader<D extends ISerializable<CompoundTag>> extends SimpleJsonResourceReloadListener implements SyncedResourceManager.ISyncedData {

    private final Map<ResourceLocation, D> resources = new HashMap<>();
    private final Function<JsonObject, D> jsonReader;
    private final Function<CompoundTag, D> tagReader;
    private final ResourceLocation id;

    public JsonResourceReloader(ResourceLocation id, String directory, Function<JsonObject, D> jsonReader, Function<CompoundTag, D> tagReader) {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), directory);
        this.jsonReader = jsonReader;
        this.tagReader = tagReader;
        this.id = id;
    }

    protected void apply(Map<ResourceLocation, JsonElement> pJsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        pJsonMap.forEach(((resourceLocation, jsonElement) -> resources.put(resourceLocation, jsonReader.apply(jsonElement.getAsJsonObject()))));
    }

    public void reload(final Map<ResourceLocation, D> resources) {
        this.resources.clear();
        this.resources.putAll(resources);
    }

    public Set<Map.Entry<ResourceLocation, D>> sets() {
        return resources.entrySet();
    }

    public D get(ResourceLocation id) {
        return resources.get(id);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public FriendlyByteBuf writeData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.resources.size());
        this.resources.forEach((id, resource) -> {
            buffer.writeResourceLocation(id);
            buffer.writeNbt(resource.writeTag());
        });
        return buffer;
    }

    public boolean readData(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        if(size > 0) {
            ImmutableMap.Builder<ResourceLocation, D> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++) builder.put(buffer.readResourceLocation(), tagReader.apply(buffer.readNbt()));
            reload(builder.build());
        }
        return true;
    }
}