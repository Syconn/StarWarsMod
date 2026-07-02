package mod.syconn.swm.features.lightsaber.network;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ChangeLightsaberHSVPacket extends Packet<ChangeLightsaberHSVPacket> {

    private final BlockPos pos;
    private final int hsv;
    private final int blade;

    public ChangeLightsaberHSVPacket(BlockPos pos, int hsv, int blade) {
        this.pos = pos;
        this.hsv = hsv;
        this.blade = blade;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.hsv);
        buf.writeInt(this.blade);
    }

    @Override
    public void encode(ChangeLightsaberHSVPacket message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.hsv);
        buffer.writeInt(message.blade);
    }

    @Override
    public ChangeLightsaberHSVPacket decode(FriendlyByteBuf buffer) {
        return new ChangeLightsaberHSVPacket(buffer.readBlockPos(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void handle(ChangeLightsaberHSVPacket message, PacketContext context) {
        context.execute(() -> {
            if (context.getPlayer().level().getBlockEntity(message.pos) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
                LightsaberTag.update(blockEntity.getContainer().getItem(0), t -> {
                    if (message.blade != -1) t.setColor(message.blade, message.hsv);
                    else t.setColor(message.hsv);
                });
                blockEntity.setChanged();
            }
        });
        context.setHandled(true);
    }
}
