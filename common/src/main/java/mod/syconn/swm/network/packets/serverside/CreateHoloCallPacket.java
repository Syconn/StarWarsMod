package mod.syconn.swm.network.packets.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.nbt.NbtTools;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.function.Supplier;

public class CreateHoloCallPacket {

    private final List<HologramNetwork.Caller> callers;

    public CreateHoloCallPacket(List<HologramNetwork.Caller> callers) {
        this.callers = callers;
    }

    public CreateHoloCallPacket(FriendlyByteBuf buf) {
        this.callers = NbtTools.getArray(buf.readNbt(), HologramNetwork.Caller::from);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(NbtTools.putArray(this.callers, HologramNetwork.Caller::save));
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> { if (context.get().getPlayer() instanceof ServerPlayer sp) HologramNetwork.get(sp.serverLevel()).createCall(this.callers.remove(0), this.callers); });
    }
}
