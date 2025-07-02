package mod.syconn.swm.server.savedata;

import mod.syconn.swm.utils.block.WorldPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HologramNetwork extends SavedData {

    private static final String tagID = "hologram_network";
    private final ServerLevel level;

    private final Map<UUID, List<Call>> CALLS = new HashMap<>();

    public HologramNetwork(ServerLevel level) {
        this.level = level;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }

    public static HologramNetwork load(ServerLevel serverLevel, CompoundTag pTag) {
        return create(serverLevel);
    }

    private static HologramNetwork create(ServerLevel serverLevel) {
        return new HologramNetwork(serverLevel);
    }

    public static HologramNetwork get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(t -> load(server, t), () -> create(server), tagID);
    }

    public record Call(Caller owner, List<Caller> participants) {

    }

    public record Caller(UUID uuid, Optional<WorldPos> location, boolean handheld) {

    }
}
