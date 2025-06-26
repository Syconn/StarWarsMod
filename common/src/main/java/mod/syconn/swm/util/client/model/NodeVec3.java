package mod.syconn.swm.util.client.model;

import com.google.gson.JsonObject;
import com.mojang.math.Quaternion;
import net.minecraft.nbt.CompoundTag;

public class NodeVec3 {

    public final double x;
    public final double y;
    public final double z;
    public final Quaternion q;
    public final float scalar;

    public NodeVec3(double x, double y, double z) {
        this(x, y, z, new Quaternion(0, 0, 0, 1.0f), 1.0f);
    }

    public NodeVec3(double x, double y, double z, Quaternion q) {
        this(x, y, z, q, 1.0f);
    }

    public NodeVec3(double x, double y, double z, Quaternion q, float scalar) {
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
        json.addProperty("qx", node.q.i());
        json.addProperty("qy", node.q.j());
        json.addProperty("qz", node.q.k());
        json.addProperty("qw", node.q.r());
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
        return new NodeVec3(x, y, z, new Quaternion(qx, qy, qz, qw), scalar);
    }

    public static NodeVec3 getNode(CompoundTag tag) {
        return new NodeVec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), new Quaternion(tag.getFloat("qx"), tag.getFloat("qy"), tag.getFloat("qz"), tag.getFloat("qw")),
                tag.getFloat("scalar"));
    }

    public static CompoundTag putNode(NodeVec3 node) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", node.x);
        tag.putDouble("y", node.y);
        tag.putDouble("z", node.z);
        tag.putFloat("qx", node.q.i());
        tag.putFloat("qy", node.q.j());
        tag.putFloat("qz", node.q.k());
        tag.putFloat("qw", node.q.r());
        tag.putFloat("scalar", node.scalar);
        return tag;
    }
}
