package mod.syconn.swm.util.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.ReloadListenerRegistry;
import io.netty.buffer.Unpooled;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.SyncResourceDataPacket;
import mod.syconn.swm.util.json.JsonResourceReloader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.HashMap;
import java.util.Map;

public class SyncedResourceManager {

    private static final Map<ResourceLocation, ISyncedData> SYNCED_DATA = new HashMap<>();

    public static void register(ISyncedData syncResources) {
        SYNCED_DATA.putIfAbsent(syncResources.getId(), syncResources);
    }

    public static void register(JsonResourceReloader<?> syncResources) {
        SYNCED_DATA.putIfAbsent(syncResources.getId(), syncResources);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, (PreparableReloadListener) SYNCED_DATA.get(syncResources.getId()));
    }

    public static void handleJoin(ServerPlayer player) {
        SYNCED_DATA.forEach((id, data) -> NetworkManager.sendToPlayer(player, new SyncResourceDataPacket(id, data.writeData())));
    }

    public static ISyncedData getLoginDataSupplier(ResourceLocation id) {
        return SYNCED_DATA.get(id);
    }

    public interface ISyncedData {

        ResourceLocation getId();
        CompoundTag writeData();
        boolean readData(CompoundTag tag);
    }
}
