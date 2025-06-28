package mod.syconn.swm.util.codec;

import io.netty.buffer.ByteBuf;
import mod.syconn.swm.util.client.model.NodeVec3;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class StreamCodecs {

    public static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.composite(ByteBufCodecs.DOUBLE, Vec3::x, ByteBufCodecs.DOUBLE, Vec3::y, ByteBufCodecs.DOUBLE, Vec3::z, Vec3::new);
    public static final StreamCodec<ByteBuf, Quaternionf> QUATERNIONF = StreamCodec.composite(ByteBufCodecs.FLOAT, Quaternionf::x, ByteBufCodecs.FLOAT, Quaternionf::y, ByteBufCodecs.FLOAT,
            Quaternionf::z, ByteBufCodecs.FLOAT, Quaternionf::w, Quaternionf::new);
    public static final StreamCodec<ByteBuf, NodeVec3> NODE_VEC3 = StreamCodec.composite(ByteBufCodecs.DOUBLE, NodeVec3::getX, ByteBufCodecs.DOUBLE, NodeVec3::getY, ByteBufCodecs.DOUBLE,
            NodeVec3::getZ, QUATERNIONF, NodeVec3::getQuaternionf, ByteBufCodecs.FLOAT, NodeVec3::getScalar, NodeVec3::new);

    public static <T extends Enum<T>> StreamCodec<ByteBuf, T> enumCodec(Class<T> type) {
        return new StreamCodec<>() {
            @Override
            public T decode(ByteBuf byteBuf) {
                return type.getEnumConstants()[byteBuf.readInt()];
            }

            @Override
            public void encode(ByteBuf byteBuf, T type) {
                byteBuf.writeInt(type.ordinal());
            }
        };
    }
}
