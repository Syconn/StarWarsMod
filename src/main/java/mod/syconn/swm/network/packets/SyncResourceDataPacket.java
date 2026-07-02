package mod.syconn.swm.network.packets;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.utils.server.SyncedResourceManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SyncResourceDataPacket extends Packet<SyncResourceDataPacket> {

    private final ResourceLocation id;
    private final CompoundTag data;

    public SyncResourceDataPacket(ResourceLocation id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.id);
        buf.writeNbt(this.data);
    }

    @Override
    public void encode(SyncResourceDataPacket message, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(message.id);
        buffer.writeNbt(message.data);
    }

    @Override
    public SyncResourceDataPacket decode(FriendlyByteBuf buffer) {
        return new SyncResourceDataPacket(buffer.readResourceLocation(), buffer.readNbt());
    }

    @Override
    public void handle(SyncResourceDataPacket message, PacketContext context) {
        context.execute(() -> {
            SyncedResourceManager.ISyncedData data = SyncedResourceManager.getLoginDataSupplier(this.id);
            data.readData(this.data);
        });
        context.setHandled(true);
    }
}
