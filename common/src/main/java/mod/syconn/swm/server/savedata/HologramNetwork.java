package mod.syconn.swm.server.savedata;

import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.nbt.NbtTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class HologramNetwork extends SavedData {

    private static final String tagID = "hologram_network";

    private final List<Call> CALLS = new ArrayList<>();

    public HologramNetwork() {}

    public void createCall(Caller caller, List<Caller> callers) {
        this.CALLS.add(new Call(caller, callers));
    }

    public void modifyCall(int call, Function<Call, Call> function) {
        this.CALLS.set(call, function.apply(this.CALLS.get(call)));
    }

    public List<Call> getCalls(UUID player) {
        return CALLS.stream().filter(call -> canJoinCall(player, call)).toList();
    }

    private boolean canJoinCall(UUID player, Call call) {
        return call.owner.uuid.equals(player) || !call.participants.stream().filter(caller -> caller.uuid.equals(player)).toList().isEmpty();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.put("calls", NbtTools.putArray(this.CALLS, Call::save));
        return compoundTag;
    }

    public void read(CompoundTag tag) {
        if(tag.contains(tagID, Tag.TAG_LIST)) tag.getList(tagID, Tag.TAG_COMPOUND).forEach(nbt -> CALLS.addAll(NbtTools.getArray(tag.getCompound("calls"), Call::from)));
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
            tag.put("location", NbtTools.putNullable(location, WorldPos::save));
            tag.putBoolean("handheld", this.handheld);
            return  tag;
        }
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
}
