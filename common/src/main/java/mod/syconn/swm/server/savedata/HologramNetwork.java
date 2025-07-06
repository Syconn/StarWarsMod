package mod.syconn.swm.server.savedata;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.general.ListUtil;
import mod.syconn.swm.utils.general.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HologramNetwork extends SavedData {

    private static final String tagID = "hologram_network";

    private final Map<UUID, Call> CALLS = new HashMap<>();

    public HologramNetwork() { }

    public void createCall(Caller caller, List<Caller> callers) {
//        this.CALLS.clear(); // TODO REMOVE FOR AFTER TESTING

//        if (this.CALLS.containsKey(caller.uuid)) this.endCall(caller.uuid); // TODO WORKING CODE
//        this.CALLS.put(caller.uuid, new Call(caller.uuid, caller, callers.stream().collect(Collectors.toMap(Caller::uuid, c -> c))));
//        this.setDirty();

//        if (this.CALLS.containsKey(caller.uuid)) { // TODO MORE TESTING CODE
//            var c = this.CALLS.get(caller.uuid).owner;
//            this.CALLS.put(caller.uuid, new Call(caller.uuid, caller, Map.of(c.uuid, c)));
//        } else this.CALLS.put(caller.uuid, new Call(caller.uuid, caller, callers.stream().collect(Collectors.toMap(Caller::uuid, c -> c))));
//        this.setDirty();

        var call = this.CALLS.get(caller.uuid);
        var calls = ListUtil.add(call.owner, call.participants.values().stream().toList());
        calls.forEach(c -> {
            var blockEntity = GameInstance.getServer().getLevel(c.location.level()).getBlockEntity(c.location.pos());
            if (c.location != null && blockEntity instanceof HoloProjectorBlockEntity projector) projector.joinCall(calls);
        });
    }

    public void modifyCall(UUID callId, Function<Call, Call> function) {
        var val = function.apply(this.CALLS.get(callId));
        if (val != null) this.CALLS.put(callId, val);
        else this.CALLS.remove(callId);
        this.setDirty();
    }

    public void endCall(UUID callId) { // TODO IMPLEMENT
        this.setDirty();
    }

    public List<Call> getCalls(UUID player) {
        return this.CALLS.values().stream().filter(call -> canJoinCall(player, call)).toList();
    }

    private boolean canJoinCall(UUID player, Call call) {
        return call.owner.uuid.equals(player) || call.participants.containsKey(player);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.put("calls", NBTUtil.putMap(this.CALLS, k -> NBTUtil.convert(t -> t.putUUID("id", k)), Call::save));
        return compoundTag;
    }

    public void read(CompoundTag tag) {
        this.CALLS.putAll(NBTUtil.getMap(tag.getCompound("calls"), t -> t.getUUID("id"), Call::from));
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

        public Caller updatePos(WorldPos pos) {
            return new Caller(this.uuid, pos, this.handheld);
        }

        public Caller updateHandheld(boolean held) {
            return new Caller(this.uuid, this.location, held);
        }

        public static Caller from(CompoundTag tag) {
            return new Caller(tag.getUUID("uuid"), NBTUtil.getNullable(tag.getCompound("location"), WorldPos::from), tag.getBoolean("handheld"));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("uuid", this.uuid);
            tag.put("location", NBTUtil.putNullable(this.location, WorldPos::save));
            tag.putBoolean("handheld", this.handheld);
            return  tag;
        }
    }

    public record Call(UUID id, Caller owner, Map<UUID, Caller> participants) {

        public Call updateOwner(Caller caller) {
            return new Call(this.id, caller, this.participants);
        }

        public Call updateParticipants(Consumer<Map<UUID, Caller>> consumer) {
            consumer.accept(participants);
            return new Call(this.id, this.owner, participants);
        }

        public static Call from(CompoundTag tag) {
            return new Call(tag.getUUID("id"), Caller.from(tag.getCompound("owner")), NBTUtil.getMap(tag.getCompound("participants"), t -> t.getUUID("id"), Caller::from));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("id", this.id);
            tag.put("owner", this.owner.save());
            tag.put("participants", NBTUtil.putMap(this.participants, k -> NBTUtil.convert(t -> t.putUUID("id", k)), Caller::save));
            return  tag;
        }
    }
}
