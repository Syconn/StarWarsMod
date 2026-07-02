package mod.syconn.swm.api.network;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.loaders.fabric.network.FabricNetworkBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.function.Consumer;

public class NetworkManager {

    public static NetworkManager.INetworkBuilder createNetwork(String id) {
        return new FabricNetworkBuilder(id);
    }

    public static OptionalInt openMenuWithData(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> data) {
        return player.openMenu(new ExtendedScreenHandlerFactory() {
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer) {
                data.accept(buffer);
            }

            public @NotNull Component getDisplayName() {
                return provider.getDisplayName();
            }

            public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory playerInventory, @NotNull Player player) {
                return provider.createMenu(windowId, playerInventory, player);
            }
        });
    }

    public interface INetworkBuilder {
        <T extends Packet<T>> INetworkBuilder registerPlayMessage(Class<T> messageClass);
        <T extends Packet<T>> INetworkBuilder registerPlayMessage(Class<T> messageClass, @Nullable PacketDirection direction);
        INetwork build();
    }
}
