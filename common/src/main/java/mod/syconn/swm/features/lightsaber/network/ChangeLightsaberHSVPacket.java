package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class ChangeLightsaberHSVPacket {

    private final BlockPos pos;
    private final int hsv;
    private final int blade;

    public ChangeLightsaberHSVPacket(BlockPos pos, int hsv, int blade) {
        this.pos = pos;
        this.hsv = hsv;
        this.blade = blade;
    }

    public ChangeLightsaberHSVPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.hsv);
        buf.writeInt(this.blade);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer().level().getBlockEntity(this.pos) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
                LightsaberTag.update(blockEntity.getContainer().getItem(0), t -> {
                    if (this.blade != -1) t.setColor(this.blade, this.hsv);
                    else t.setColor(this.hsv);
                });
                blockEntity.setChanged();
            }
        });
    }
}
