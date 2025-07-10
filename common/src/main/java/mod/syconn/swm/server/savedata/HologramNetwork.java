package mod.syconn.swm.server.savedata;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import mod.syconn.swm.core.ModBlockEntities;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.clientside.NotifyPlayerPacket;
import mod.syconn.swm.utils.block.WorldPos;
import mod.syconn.swm.utils.generic.ListUtil;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HologramNetwork extends SavedData {

    private static final String tagID = "hologram_network";

    private final Map<UUID, Call> CALLS = new HashMap<>();
    private final Map<UUID, UUID> ITEMS = new HashMap<>();
    private final Map<UUID, List<WorldPos>> BLOCKS = new HashMap<>();
    private final Map<WorldPos, List<UUID>> RENDERABLES = new HashMap<>();

    public HologramNetwork() { }

    public void createCall(Caller caller, List<Caller> callers) {
        this.CALLS.clear(); // TODO REMOVE FOR AFTER TESTING
        this.ITEMS.clear(); // TODO REMOVE FOR AFTER TESTING
        this.BLOCKS.clear(); // TODO REMOVE FOR AFTER TESTING
        this.RENDERABLES.clear(); // TODO REMOVE FOR AFTER TESTING

        if (this.CALLS.containsKey(caller.uuid)) this.leaveCall(caller.uuid, caller);
        this.CALLS.put(caller.uuid, new Call(caller.uuid, caller, callers.stream().collect(Collectors.toMap(Caller::uuid, c -> c))));
        if (caller.item != null) this.ITEMS.put(caller.item, caller.uuid);
        if (caller.location != null) {
            this.BLOCKS.put(caller.uuid, List.of(caller.location));
            GameInstance.getServer().getLevel(caller.location.level()).getBlockEntity(caller.location.pos(), ModBlockEntities.HOLO_PROJECTOR.get()).ifPresent(b -> b.addCall(caller.uuid));
        }
        this.setDirty();
        this.notifyPlayers(caller, callers);
    }

    public void connect(UUID callId, Caller caller) {
        this.CALLS.computeIfPresent(callId, ((uuid, call) -> call.updateParticipants(map -> map.put(caller.uuid, caller))));
        if (caller.item != null) this.ITEMS.put(caller.item, callId);
        if (caller.location != null) {
            this.BLOCKS.put(callId, ListUtil.add(caller.location, this.BLOCKS.get(callId)));
            GameInstance.getServer().getLevel(caller.location.level()).getBlockEntity(caller.location.pos(), ModBlockEntities.HOLO_PROJECTOR.get()).ifPresent(b -> b.addCall(caller.uuid));
        }
        this.setDirty();
    }

    public void leaveCall(UUID callId, Caller caller) {
        var call = this.CALLS.get(callId);
        if (call != null) {
            if (caller.item != null) this.ITEMS.remove(caller.item);
            if (caller.location != null) {
                this.BLOCKS.put(callId, ListUtil.remove(caller.location, this.BLOCKS.get(callId)));
                GameInstance.getServer().getLevel(caller.location.level()).getBlockEntity(caller.location.pos(), ModBlockEntities.HOLO_PROJECTOR.get()).ifPresent(b -> b.addCall(null));
            }
            if (this.BLOCKS.get(callId) != null && this.BLOCKS.get(callId).isEmpty()) this.BLOCKS.remove(callId);

            if (call.owner.uuid.equals(caller.uuid)) {
                this.CALLS.remove(callId);
                call.participants.values().forEach(c -> {
                    if (c.item != null) this.ITEMS.remove(c.item);
                    if (caller.location != null) {
                        this.BLOCKS.put(callId, ListUtil.remove(caller.location, this.BLOCKS.get(callId)));
                        GameInstance.getServer().getLevel(caller.location.level()).getBlockEntity(caller.location.pos(), ModBlockEntities.HOLO_PROJECTOR.get()).ifPresent(b -> b.addCall(null));
                    }
                });
            } else if (this.CALLS.get(callId).participants().containsKey(caller.uuid))
                this.CALLS.compute(callId, ((uuid, c) -> c.updateParticipants(map -> map.remove(caller.uuid))));

            if (this.CALLS.containsKey(callId) && this.CALLS.get(callId).participants.isEmpty()) this.leaveCall(callId, call.owner);
            this.setDirty();
        }
    }

    public void blockRemoved(UUID callId, WorldPos worldPos) {
        var call = this.CALLS.get(callId);
        if (call != null) {
            var others = Map.copyOf(call.participants);
            if (call.owner.location.equals(worldPos)) this.leaveCall(callId, call.owner);
            others.values().stream().filter(c -> c.location.equals(worldPos)).forEach(c -> this.leaveCall(callId, c));
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
        compoundTag.put("blocks", NBTUtil.putMap(this.BLOCKS, NBTUtil::putUUID, w -> NBTUtil.putList(w, WorldPos::save)));
        compoundTag.put("renderables", NBTUtil.putMap(this.RENDERABLES, WorldPos::save, w -> NBTUtil.putList(w, NBTUtil::putUUID)));
        return compoundTag;
    }

    private void read(CompoundTag tag) {
        this.CALLS.putAll(NBTUtil.getMap(tag.getCompound("calls"), NBTUtil::getUUID, Call::from));
        this.ITEMS.putAll(NBTUtil.getMap(tag.getCompound("items"), NBTUtil::getUUID, NBTUtil::getUUID));
        this.BLOCKS.putAll(NBTUtil.getMap(tag.getCompound("blocks"), NBTUtil::getUUID, t -> NBTUtil.getList(t, WorldPos::from)));
        this.RENDERABLES.putAll(NBTUtil.getMap(tag.getCompound("renderables"), WorldPos::from, t -> NBTUtil.getList(t, NBTUtil::getUUID)));
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
        calls.forEach(((uuid, call) -> { if (call.participants.containsKey(player.getUUID())) this.leaveCall(uuid, call.participants.get(player.getUUID())); }));
    }

    public void serverTick(ServerLevel level) { // TODO MAY NEED TO DELAY HOW OFTEN THIS RUNS
        var inflate = 3.5;

        this.CALLS.forEach(((uuid, call) -> {
            var blocks = this.BLOCKS.get(uuid);
            if (blocks != null && !blocks.isEmpty()) {
                blocks.forEach(block -> {
                    var players = this.RENDERABLES.containsKey(block) ? this.RENDERABLES.get(block) : new ArrayList<UUID>();
                    var entities = level.getServer().getLevel(block.level()).getEntitiesOfClass(Player.class, new AABB(block.pos()).move(0, 1, 0).inflate(inflate)).stream().map(Entity::getUUID).toList();
                    var rem = players.stream().filter(u -> !entities.contains(u)).toList();
                    var add = entities.stream().filter(u -> !players.contains(u)).toList();
                    rem.forEach(players::remove);
                    players.addAll(add);
                    if (!rem.isEmpty() || !add.isEmpty()) {
                        this.RENDERABLES.put(block, players);
                        this.setDirty();
                    }
                });
            }
        }));

        this.CALLS.forEach(((callId, call) -> {
            var blocks = this.BLOCKS.get(callId);
            if (blocks != null && !blocks.isEmpty()) {
                blocks.forEach(block -> {
                    if (level.getServer().getLevel(block.level()).getBlockEntity(block.pos()) instanceof HoloProjectorBlockEntity blockEntity) {
                        this.RENDERABLES.entrySet().stream().filter(e -> !e.getKey().equals(block)).forEach(entry -> {
                            entry.getValue().forEach(uuid -> {
                                var player = level.getServer().getLevel(entry.getKey().level()).getPlayerByUUID(uuid);
                                blockEntity.addEntity(uuid, player == null ? new Vec3(0, 0, 0) : player.position().subtract(entry.getKey().toVector()));
                            });
                        });
                    }
                });
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
