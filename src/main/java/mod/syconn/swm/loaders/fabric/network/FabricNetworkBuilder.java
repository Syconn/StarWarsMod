package mod.syconn.swm.loaders.fabric.network;

import mod.syconn.swm.api.network.INetwork;
import mod.syconn.swm.api.network.NetworkManager;
import mod.syconn.swm.api.network.PacketDirection;
import mod.syconn.swm.api.network.message.Packet;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FabricNetworkBuilder implements NetworkManager.INetworkBuilder {
    private final String id;
    private final AtomicInteger idCount = new AtomicInteger(1);
    private final List<FabricPacket<?>> playMessages = new ArrayList<>();

    public FabricNetworkBuilder(String id) {
        this.id = id;
    }

    @Override
    public <T extends Packet<T>> NetworkManager.INetworkBuilder registerPlayMessage(Class<T> messageClass) {
        return this.registerPlayMessage(messageClass, null);
    }

    @Override
    public <T extends Packet<T>> NetworkManager.INetworkBuilder registerPlayMessage(Class<T> messageClass, @Nullable PacketDirection direction) {
        try {
            Constructor<T> constructor = messageClass.getDeclaredConstructor();
            T message = constructor.newInstance();
            this.playMessages.add(new FabricPacket<>(this.idCount.getAndIncrement(), messageClass, message::encode, message::decode, message::handle, null));
        } catch(NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("The message %s is missing an empty parameter constructor", messageClass.getName()), e);
        } catch(IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Unable to access the constructor of %s. Make sure the constructor is public.", messageClass.getName()), e);
        } catch(InvocationTargetException | InstantiationException ignored) {}
        return this;
    }

    @Override
    public INetwork build() {
        return new FabricNetwork(this.id, this.playMessages);
    }
}
