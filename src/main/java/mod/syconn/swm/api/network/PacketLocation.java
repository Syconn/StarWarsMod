package mod.syconn.swm.api.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public record PacketLocation(ServerLevel level, Vec3 pos, double range) {

    public static PacketLocation create(ServerLevel level, BlockPos pos) {
        return new PacketLocation(level, pos.getCenter(), 16);
    }

    public static PacketLocation create(ServerLevel level, BlockPos pos, double range) {
        return new PacketLocation(level, pos.getCenter(), range);
    }

    public static PacketLocation create(ServerLevel level, Vec3 pos, double range) {
        return new PacketLocation(level, pos, range);
    }

    public static PacketLocation create(ServerLevel level, double x, double y, double z, double range) {
        return new PacketLocation(level, new Vec3(x, y, z), range);
    }
}
