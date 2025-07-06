package mod.syconn.swm.network.packets.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.blockentity.HoloProjectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateHologramPacket { // TODO WRITE PLAYER

    private final BlockPos pos;
    private final UUID uuid;

    public UpdateHologramPacket(BlockPos pos, UUID uuid) {
        this.pos = pos;
        this.uuid = uuid;
    }

    public UpdateHologramPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeUUID(this.uuid);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer().level().getBlockEntity(this.pos) instanceof HoloProjectorBlockEntity blockEntity) blockEntity.setHologramData(this.uuid);
        });
    }
}
