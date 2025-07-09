package mod.syconn.swm.server.savedata;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.clientside.NotifyPlayerPacket;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.general.NBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
    private final Map<UUID, UUID> ITEMS = new HashMap<>();

    public HologramNetwork() { }

    public void createCall(Caller caller, List<Caller> callers) {
        this.CALLS.clear(); // TODO REMOVE FOR AFTER TESTING
        this.ITEMS.clear(); // TODO REMOVE FOR AFTER TESTING

        callers.stream().forEach(System.out::println);

        if (this.CALLS.containsKey(caller.uuid)) this.leaveCall(caller.uuid, caller);
        this.CALLS.put(caller.uuid, new Call(caller.uuid, caller, callers.stream().collect(Collectors.toMap(Caller::uuid, c -> c))));
        if (caller.item != null) this.ITEMS.put(caller.item, caller.uuid);
        this.setDirty();
        this.notifyPlayers(caller, callers);

//        var call = this.CALLS.get(caller.uuid);
//        var calls = ListUtil.add(call.owner, call.participants.values().stream().toList());
//        calls.forEach(c -> {
//            var blockEntity = GameInstance.getServer().getLevel(c.location.level()).getBlockEntity(c.location.pos());
//            if (c.location != null && blockEntity instanceof HoloProjectorBlockEntity projector) projector.joinCall(calls);
//        });
    }

    public void connect(UUID callId, Caller caller) {
        this.CALLS.computeIfPresent(callId, ((uuid, call) -> call.updateParticipants(map -> map.put(caller.uuid, caller))));
        if (caller.item != null) this.ITEMS.put(caller.item, callId);
        this.setDirty();
    }

    public void leaveCall(UUID callId, Caller caller) {
        var call = this.CALLS.get(callId);
        if (call != null) {
            if (caller.item != null) this.ITEMS.remove(caller.item);

            if (call.owner.uuid.equals(caller.uuid)) {
                this.CALLS.remove(callId);
                call.participants.values().forEach(c -> { if (c.item != null) this.ITEMS.remove(c.item); });
            } else if (this.CALLS.get(callId).participants().containsKey(caller.uuid)) {
                this.CALLS.compute(callId, ((uuid, c) -> c.updateParticipants(map -> map.remove(caller.uuid))));
            }

            if (this.CALLS.containsKey(callId) && this.CALLS.get(callId).participants.isEmpty()) this.leaveCall(callId, call.owner);
            this.setDirty();
        }
    }

    public void notifyPlayers(Caller caller, List<Caller> callers) {
        callers.stream().forEach(c -> {
            if (GameInstance.getServer() != null) {
                var owner = GameInstance.getServer().getPlayerList().getPlayer(caller.uuid);
                var serverPlayer = GameInstance.getServer().getPlayerList().getPlayer(c.uuid);
                if (serverPlayer != null && owner != null) Network.CHANNEL.sendToPlayer(serverPlayer, new NotifyPlayerPacket(Component.literal(
                            "Incoming Transmission from " + owner.getName().getString()).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)));
            }
        });
    }

    public Call getCall(UUID callId) {
        return this.CALLS.get(callId);
    }

    public List<Call> getCalls(UUID player) {
        return this.CALLS.values().stream().filter(call -> canJoinCall(player, call)).toList();
    }

    public UUID getCallId(UUID itemId) {
        return this.ITEMS.get(itemId);
    }

    private boolean canJoinCall(UUID player, Call call) {
        return call.owner.uuid.equals(player) || call.participants.containsKey(player);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        compoundTag.put("calls", NBTUtil.putMap(this.CALLS, NBTUtil::putUUID, Call::save));
        compoundTag.put("items", NBTUtil.putMap(this.ITEMS, NBTUtil::putUUID, NBTUtil::putUUID));
        return compoundTag;
    }

    private void read(CompoundTag tag) {
        this.CALLS.putAll(NBTUtil.getMap(tag.getCompound("calls"), NBTUtil::getUUID, Call::from));
        this.ITEMS.putAll(NBTUtil.getMap(tag.getCompound("items"), NBTUtil::getUUID, NBTUtil::getUUID));
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

    public void playerLeave(ServerPlayer player) {
        if (this.CALLS.containsKey(player.getUUID())) this.leaveCall(player.getUUID(), this.CALLS.get(player.getUUID()).owner);

        var calls = Map.copyOf(this.CALLS);
        calls.forEach(((uuid, call) -> {
            if (call.participants.containsKey(player.getUUID())) {
                this.leaveCall(uuid, call.participants.get(player.getUUID()));
            }
        }));
    }

    public record Caller(UUID uuid, @Nullable UUID item, @Nullable WorldPos location) {

        @Override
        public @NotNull String toString() { // TODO FOR TESTING
            return "UUID-" + uuid + " ITEM-" + item + " WORLD-" + location;
        }

        public static Caller from(CompoundTag tag) {
            return new Caller(tag.getUUID("uuid"), NBTUtil.getNullable(tag.getCompound("item"), NBTUtil::getUUID), NBTUtil.getNullable(tag.getCompound("location"), WorldPos::from));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("uuid", this.uuid);
            tag.put("item", NBTUtil.putNullable(this.item, NBTUtil::putUUID));
            tag.put("location", NBTUtil.putNullable(this.location, WorldPos::save));
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
            return new Call(tag.getUUID("id"), Caller.from(tag.getCompound("owner")), NBTUtil.getMap(tag.getCompound("participants"), NBTUtil::getUUID, Caller::from));
        }

        public CompoundTag save() {
            var tag = new CompoundTag();
            tag.putUUID("id", this.id);
            tag.put("owner", this.owner.save());
            tag.put("participants", NBTUtil.putMap(this.participants, NBTUtil::putUUID, Caller::save));
            return  tag;
        }
    }
}
