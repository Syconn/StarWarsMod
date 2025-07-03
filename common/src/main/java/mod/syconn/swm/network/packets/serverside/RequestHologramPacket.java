package mod.syconn.swm.network.packets.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.clientside.RequestedHologramPacket;
import mod.syconn.swm.server.savedata.HologramNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class RequestHologramPacket {

    public RequestHologramPacket() { }

    public RequestHologramPacket(FriendlyByteBuf buf) { }

    public void encode(FriendlyByteBuf buf) { }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() instanceof ServerPlayer sp) Network.CHANNEL.sendToPlayer(sp, new RequestedHologramPacket(HologramNetwork.get(sp.serverLevel()).save(new CompoundTag())));
        });
    }
}
