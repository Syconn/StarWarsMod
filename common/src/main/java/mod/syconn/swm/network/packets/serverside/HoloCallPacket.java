package mod.syconn.swm.network.packets.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.server.savedata.HologramNetwork;
import mod.syconn.swm.utils.general.NBTUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.function.Supplier;

public class HoloCallPacket {

    private final List<HologramNetwork.Caller> callers;
    private final Type type;

    public HoloCallPacket(Type type, List<HologramNetwork.Caller> callers) {
        this.type = type;
        this.callers = callers;
    }

    public HoloCallPacket(FriendlyByteBuf buf) {
        this.type = buf.readEnum(Type.class);
        this.callers = NBTUtil.getList(buf.readNbt(), HologramNetwork.Caller::from);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeNbt(NBTUtil.putList(this.callers, HologramNetwork.Caller::save));
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() instanceof ServerPlayer sp) {
                if (this.type == Type.CREATE) HologramNetwork.get(sp.serverLevel()).createCall(this.callers.remove(0), this.callers);
                else if (this.type == Type.CONNECT) ;
                else ;
            }
        });
    }

    public enum Type {
        CREATE,
        CONNECT,
        LEAVE
    }
}
