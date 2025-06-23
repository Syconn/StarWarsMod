package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class ChangeLightsaberColor {

    private final BlockPos pos;
    private final int hsv;

    public ChangeLightsaberColor(BlockPos pos, int hsv) {
        this.pos = pos;
        this.hsv = hsv;
    }

    public ChangeLightsaberColor(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.hsv);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer().level().getBlockEntity(this.pos) instanceof LightsaberWorkbenchBlockEntity blockEntity) {
                LightsaberTag.update(blockEntity.getContainer().getItem(0), t -> t.color = this.hsv);
                blockEntity.markDirty();
            }
        });
    }
}
