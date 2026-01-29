package mod.syconn.swm.network.packets.clientside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.features.blaster.BlasterUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ScorchBlockPacket {

    private Vector3f pos, incident, normal;
    private boolean energyScorch;

    public ScorchBlockPacket(Vec3 pos, Vec3 incident, Vec3 normal, boolean energyScorch) {
        this.pos = pos.toVector3f();
        this.incident = incident.toVector3f();
        this.normal = normal.toVector3f();
        this.energyScorch = energyScorch;
    }

    public ScorchBlockPacket(FriendlyByteBuf buf) {
        this.pos = buf.readVector3f();
        this.incident = buf.readVector3f();
        this.normal = buf.readVector3f();
        this.energyScorch = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVector3f(this.pos);
        buf.writeVector3f(this.incident);
        buf.writeVector3f(this.normal);
        buf.writeBoolean(this.energyScorch);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> BlasterUtil.createScorchParticles(context.get().getPlayer().level(), new Vec3(this.pos), new Vec3(this.incident), new Vec3(this.normal), this.energyScorch));
    }
}
