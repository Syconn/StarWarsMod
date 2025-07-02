package mod.syconn.swm.server.savedata;

import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HologramNetwork extends SavedData {

    private static final String tagID = "hologram_network";

    private final Map<UUID, List<Call>> CALLS = new HashMap<>();

    public HologramNetwork() {}

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        var map = new ListTag();
        for (var entry : this.CALLS.entrySet()) {
            var tag = new CompoundTag();
            tag.putUUID("uuid", entry.getKey());
            tag.put("calls", NbtTools.putArray(entry.getValue(), Call::save));
            map.add(tag);
        }
        compoundTag.put(tagID, map);
        return compoundTag;
    }

    public void read(CompoundTag tag) {
        if(tag.contains(tagID, Tag.TAG_LIST)) {
            tag.getList(tagID, Tag.TAG_COMPOUND).forEach(nbt -> {
                var compoundTag = (CompoundTag) nbt;
                CALLS.put(compoundTag.getUUID("uuid"), NbtTools.getArray(tag.getCompound("calls"), Call::from));
            });
        }
    }

    public static HologramNetwork load(CompoundTag tag) {
        var network = create();
        network.read(tag);
        return network;
    }

    private static HologramNetwork create() {
        return new HologramNetwork();
    }

    public static HologramNetwork get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(HologramNetwork::load, HologramNetwork::create, tagID);
    }

    public record Call(Caller owner, List<Caller> participants) {
        public static Call from(CompoundTag tag) {
            return new Call(Caller.from(tag.getCompound("owner")), NbtTools.getArray(tag.getCompound("participants"), Caller::from));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.put("owner", owner.save());
            tag.put("participants", NbtTools.putArray(participants, Caller::save));
            return  tag;
        }
    }

    public record Caller(UUID uuid, Optional<WorldPos> location, boolean handheld) {
        public static Caller from(CompoundTag tag) {
            return new Caller(tag.getUUID("uuid"), NbtTools.getOptional(tag.getCompound("location"), WorldPos::from), tag.getBoolean("handheld"));
        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("uuid", this.uuid);
            tag.put("location", NbtTools.putOptional(location, t -> t.get().save()));
            tag.putBoolean("handheld", this.handheld);
            return  tag;
        }
    }
}
