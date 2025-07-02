package mod.syconn.swm.network.packets;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.server.savedata.HologramNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class CreateHoloCallPacket {

    public CreateHoloCallPacket() {

    }

    public CreateHoloCallPacket(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() instanceof ServerPlayer sp) {
                HologramNetwork.get(sp.serverLevel());
            }
        });
    }
}
