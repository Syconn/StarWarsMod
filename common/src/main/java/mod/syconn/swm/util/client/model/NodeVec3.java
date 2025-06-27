package mod.syconn.swm.util.client.model;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import org.joml.Quaternionf;

public class NodeVec3 {

    public final double x;
    public final double y;
    public final double z;
    public final Quaternionf q;
    public final float scalar;

    public NodeVec3(double x, double y, double z) {
        this(x, y, z, new Quaternionf(0, 0, 0, 1.0f), 1.0f);
    }

    public NodeVec3(double x, double y, double z, Quaternionf q) {
        this(x, y, z, q, 1.0f);
    }

    public NodeVec3(double x, double y, double z, Quaternionf q, float scalar) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.q = q;
        this.scalar = scalar;
    }

    public static JsonObject addNode(NodeVec3 node) {
        var json = new JsonObject();
        json.addProperty("x", node.x);
        json.addProperty("y", node.y);
        json.addProperty("z", node.z);
        json.addProperty("qx", node.q.x);
        json.addProperty("qy", node.q.y);
        json.addProperty("qz", node.q.z);
        json.addProperty("qw", node.q.w);
        json.addProperty("scalar", node.scalar);
        return json;
    }

    public static NodeVec3 getNode(JsonObject json) {
        var x = json.get("x").getAsDouble();
        var y = json.get("y").getAsDouble();
        var z = json.get("z").getAsDouble();
        var qx = json.get("qx").getAsFloat();
        var qy = json.get("qy").getAsFloat();
        var qz = json.get("qz").getAsFloat();
        var qw = json.get("qw").getAsFloat();
        var scalar = json.get("scalar").getAsFloat();
        return new NodeVec3(x, y, z, new Quaternionf(qx, qy, qz, qw), scalar);
    }

    public static NodeVec3 getNode(CompoundTag tag) {
        return new NodeVec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), new Quaternionf(tag.getFloat("qx"), tag.getFloat("qy"), tag.getFloat("qz"), tag.getFloat("qw")),
                tag.getFloat("scalar"));
    }

    public static CompoundTag putNode(NodeVec3 node) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", node.x);
        tag.putDouble("y", node.y);
        tag.putDouble("z", node.z);
        tag.putFloat("qx", node.q.x);
        tag.putFloat("qy", node.q.y);
        tag.putFloat("qz", node.q.z);
        tag.putFloat("qw", node.q.w);
        tag.putFloat("scalar", node.scalar);
        return tag;
    }
}
