package mod.syconn.swm.server.savedata;

import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class HologramNetwork extends SavedData {

    private static final String tagID = "hologram_network";

    private final Map<UUID, Call> CALLS = new HashMap<>();

    public HologramNetwork() {}

    public void createCall(Caller caller, List<Caller> callers) {
        if (this.CALLS.containsKey(caller.uuid)) this.endCall(caller.uuid);
        this.CALLS.put(caller.uuid, new Call(caller.uuid, caller, callers));
    }

    public void modifyCall(UUID callId, Function<Call, Call> function) {
        this.CALLS.put(callId, function.apply(this.CALLS.get(callId)));
    }

    private void endCall(UUID callId) { // TODO IMPLEMENT

    }

    public List<Call> getCalls(UUID player) {
        return this.CALLS.values().stream().filter(call -> canJoinCall(player, call)).toList();
    }

    private boolean canJoinCall(UUID player, Call call) {
        return call.owner.uuid.equals(player) || !call.participants.stream().filter(caller -> caller.uuid.equals(player)).toList().isEmpty();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.put("calls", NbtTools.putMap(this.CALLS, k -> NbtTools.convert(t -> t.putUUID("id", k)), Call::save));
        return compoundTag;
    }

    public void read(CompoundTag tag) {
        this.CALLS.putAll(NbtTools.getMap(tag.getCompound("calls"), t -> t.getUUID("id"), Call::from));
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

    public record Caller(UUID uuid, @Nullable WorldPos location, boolean handheld) {
        public static Caller from(CompoundTag tag) {
            return new Caller(tag.getUUID("uuid"), NbtTools.getNullable(tag.getCompound("location"), WorldPos::from), tag.getBoolean("handheld"));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("uuid", this.uuid);
            tag.put("location", NbtTools.putNullable(this.location, WorldPos::save));
            tag.putBoolean("handheld", this.handheld);
            return  tag;
        }
    }

    public record Call(UUID id, Caller owner, List<Caller> participants) {
        public static Call from(CompoundTag tag) {
            return new Call(tag.getUUID("id"), Caller.from(tag.getCompound("owner")), NbtTools.getList(tag.getCompound("participants"), Caller::from));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("id", this.id);
            tag.put("owner", this.owner.save());
            tag.put("participants", NbtTools.putList(this.participants, Caller::save));
            return  tag;
        }
    }
}
