package mod.syconn.swm.network.packets.clientside;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.screen.HologramScreen;
import mod.syconn.swm.server.savedata.HologramNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class RequestedHologramPacket {

    private final CompoundTag tag;

    public RequestedHologramPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public RequestedHologramPacket(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> { if (GameInstance.getClient().screen instanceof HologramScreen screen) screen.hologramData(HologramNetwork.load(this.tag)); });
    }
}
