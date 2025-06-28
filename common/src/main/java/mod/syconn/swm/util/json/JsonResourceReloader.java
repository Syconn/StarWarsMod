package mod.syconn.swm.util.json;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.syconn.swm.util.nbt.ISerializable;
import mod.syconn.swm.util.server.SyncedResourceManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    public Optional<D> get(ResourceLocation id) {
        return Optional.ofNullable(resources.get(id));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public CompoundTag writeData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", this.resources.size());
        ListTag list = new ListTag();
        this.resources.forEach((id, resource) -> {
            CompoundTag innerTag = new CompoundTag();
            innerTag.putString("id", id.toString());
            innerTag.put("data", resource.writeTag());
            list.add(innerTag);
        });
        tag.put("list", list);
        return tag;
    }

    public boolean readData(CompoundTag tag) {
        int size = tag.getInt("size");
        if (size > 0) {
            var map = new ImmutableMap.Builder<ResourceLocation, D>();
            tag.getList("list", 10).stream().map(CompoundTag.class::cast).forEach(t -> map.put(ResourceLocation.parse(t.getString("id")), tagReader.apply(t.getCompound("data"))));
            reload(map.build());
            return true;
        }
        return false;
    }
}