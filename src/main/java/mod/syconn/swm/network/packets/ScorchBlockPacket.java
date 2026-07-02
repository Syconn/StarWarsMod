package mod.syconn.swm.network.packets;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.features.blaster.BlasterUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ScorchBlockPacket extends Packet<ScorchBlockPacket> {

    private final Vector3f pos;
    private final Vector3f incident;
    private final Vector3f normal;
    private final boolean energyScorch;

    public ScorchBlockPacket(Vec3 pos, Vec3 incident, Vec3 normal, boolean energyScorch) {
        this.pos = pos.toVector3f();
        this.incident = incident.toVector3f();
        this.normal = normal.toVector3f();
        this.energyScorch = energyScorch;
    }

    public ScorchBlockPacket(Vector3f pos, Vector3f incident, Vector3f normal, boolean energyScorch) {
        this.pos = pos;
        this.incident = incident;
        this.normal = normal;
        this.energyScorch = energyScorch;
    }

    @Override
    public void encode(ScorchBlockPacket message, FriendlyByteBuf buffer) {
        buffer.writeVector3f(message.pos);
        buffer.writeVector3f(message.incident);
        buffer.writeVector3f(message.normal);
        buffer.writeBoolean(message.energyScorch);
    }

    @Override
    public ScorchBlockPacket decode(FriendlyByteBuf buffer) {
        return new ScorchBlockPacket(buffer.readVector3f(), buffer.readVector3f(), buffer.readVector3f(), buffer.readBoolean());
    }

    @Override
    public void handle(ScorchBlockPacket message, PacketContext context) {
        context.execute(() -> {
            if (Minecraft.getInstance().level != null)
                BlasterUtil.createScorchParticles(Minecraft.getInstance().level, new Vec3(message.pos), new Vec3(message.incident), new Vec3(message.normal), message.energyScorch);
        });
        context.setHandled(true);
    }
}
