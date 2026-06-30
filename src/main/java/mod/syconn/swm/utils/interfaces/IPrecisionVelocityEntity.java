package mod.syconn.swm.utils.interfaces;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public interface IPrecisionVelocityEntity {

    default void onPrecisionVelocityPacket(ClientboundSetEntityMotionPacket packet) { }
}
