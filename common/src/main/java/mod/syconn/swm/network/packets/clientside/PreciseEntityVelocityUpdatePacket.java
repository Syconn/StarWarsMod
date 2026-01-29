package mod.syconn.swm.network.packets.clientside;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class PreciseEntityVelocityUpdatePacket extends ClientboundSetEntityMotionPacket {
    private final Vector3f position;
    private final Vector3f velocity;

    public PreciseEntityVelocityUpdatePacket(Entity entity) {
        this(entity.getId(), entity.position().toVector3f(), entity.getDeltaMovement());
    }

    public PreciseEntityVelocityUpdatePacket(int id, Vector3f position, Vec3 velocity) {
        super(id, velocity);
        this.position = position;
        this.velocity = velocity.toVector3f();
    }

    public PreciseEntityVelocityUpdatePacket(FriendlyByteBuf buf) {
        super(buf);
        this.position = buf.readVector3f();
        this.velocity = buf.readVector3f();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeVector3f(position);
        buf.writeVector3f(velocity);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Entity entity = context.get().getPlayer().level().getEntity(this.getId());
            if (entity != null) entity.lerpMotion((double)this.getXa() / (double)8000.0F, (double)this.getYa() / (double)8000.0F, (double)this.getZa() / (double)8000.0F);
        });
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getPosition() {
        return position;
    }
}
