package mod.syconn.swm.network.packets;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.utils.interfaces.IPrecisionVelocityEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PreciseEntityVelocityUpdatePacket extends Packet<PreciseEntityVelocityUpdatePacket> {
    private final Vector3f position;
    private final Vector3f velocity;
    private final int id;
    private final int xa;
    private final int ya;
    private final int za;

    public PreciseEntityVelocityUpdatePacket(int id, Vector3f position, Vec3 velocity) {
        this.position = position;
        this.velocity = velocity.toVector3f();
        this.id = id;
        double e = Mth.clamp(velocity.x, -3.9, 3.9);
        double f = Mth.clamp(velocity.y, -3.9, 3.9);
        double g = Mth.clamp(velocity.z, -3.9, 3.9);
        this.xa = (int)(e * (double)8000.0F);
        this.ya = (int)(f * (double)8000.0F);
        this.za = (int)(g * (double)8000.0F);
    }

    public PreciseEntityVelocityUpdatePacket(Entity entity) {
        this(entity.getId(), entity.position().toVector3f(), entity.getDeltaMovement());
    }

    public PreciseEntityVelocityUpdatePacket(Vector3f position, Vector3f velocity, int id, int xa, int ya, int za) {
        this.position = position;
        this.velocity = velocity;
        this.id = id;
        this.xa = xa;
        this.ya = ya;
        this.za = za;
    }

    public int getId() {
        return this.id;
    }

    public int getXa() {
        return this.xa;
    }

    public int getYa() {
        return this.ya;
    }

    public int getZa() {
        return this.za;
    }

    @Override
    public void encode(PreciseEntityVelocityUpdatePacket message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.id);
        buffer.writeShort(message.xa);
        buffer.writeShort(message.ya);
        buffer.writeShort(message.za);
        buffer.writeVector3f(message.position);
        buffer.writeVector3f(message.velocity);
    }

    @Override
    public PreciseEntityVelocityUpdatePacket decode(FriendlyByteBuf buffer) {
        return new PreciseEntityVelocityUpdatePacket(buffer.readVector3f(), buffer.readVector3f(), buffer.readVarInt(), buffer.readShort(), buffer.readShort(), buffer.readShort());
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void handle(PreciseEntityVelocityUpdatePacket message, PacketContext context) {
        context.execute(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(message.getId());
                if (entity != null) entity.lerpMotion((double)message.getXa() / (double)8000.0F, (double)message.getYa() / (double)8000.0F, (double)message.getZa() / (double)8000.0F);
                if (!(entity instanceof IPrecisionVelocityEntity ipe)) return;

                entity.setPos(new Vec3(message.getPosition()));
                entity.setDeltaMovement(new Vec3(message.getVelocity()));
                ipe.onPrecisionVelocityPacket(message);
            }
        });
        context.setHandled(true);
    }
}
