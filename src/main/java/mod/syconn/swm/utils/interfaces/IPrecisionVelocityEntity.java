package mod.syconn.swm.utils.interfaces;

import mod.syconn.swm.network.packets.PreciseEntityVelocityUpdatePacket;

public interface IPrecisionVelocityEntity {

    default void onPrecisionVelocityPacket(PreciseEntityVelocityUpdatePacket packet) { }
}
