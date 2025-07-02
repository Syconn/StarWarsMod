package mod.syconn.swm.server.savedata;

import mod.syconn.swm.utils.block.WorldPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

        }
        compoundTag.put(tagID, map);
        return compoundTag;
    }

    public static HologramNetwork load(CompoundTag pTag) {
        return create();
    }

    private static HologramNetwork create() {
        return new HologramNetwork();
    }

    public static HologramNetwork get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(HologramNetwork::load, HologramNetwork::create, tagID);
    }

    public record Call(Caller owner, List<Caller> participants) {
        public static Call from(CompoundTag tag) {
            return new Call();
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            return  tag;
        }
    }

    public record Caller(UUID uuid, Optional<WorldPos> location, boolean handheld) {
        public static Caller from(CompoundTag tag) {
            return new Caller();
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.put("uuid", )
            return  tag;
        }
    }
}
