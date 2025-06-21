package mod.syconn.swm.util.client.model;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public record NodeVec3(double x, double y, double z, Quaternionf q) {

    public static JsonObject addNode(NodeVec3 node) {
        var json = new JsonObject();
        json.addProperty("x", node.x);
        json.addProperty("y", node.y);
        json.addProperty("z", node.z);
        json.addProperty("qx", node.q.x);
        json.addProperty("qy", node.q.y);
        json.addProperty("qz", node.q.z);
        json.addProperty("qw", node.q.w);
        return json;
    }

    public static NodeVec3 getNode(JsonObject json) {
        var x = json.get("x").getAsDouble();
        var y = json.get("y").getAsDouble();
        var z = json.get("z").getAsDouble();
        var qx = json.get("qx").getAsFloat();
        var qy = json.get("qy").getAsFloat();
        var qz = json.get("qz").getAsFloat();
        var qw = json.get("qz").getAsFloat();
        return new NodeVec3(x, y, z, new Quaternionf(qx, qy, qz, qw));
    }

    public static NodeVec3 getNode(CompoundTag tag) {
        return new NodeVec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), new Quaternionf(tag.getFloat("qx"), tag.getFloat("qy"), tag.getFloat("qz"), tag.getFloat("qw")));
    }

    public static CompoundTag putNode(NodeVec3 vec3) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        tag.putFloat("qx", vec3.q.x);
        tag.putFloat("qy", vec3.q.y);
        tag.putFloat("qz", vec3.q.z);
        tag.putFloat("qw", vec3.q.w);
        return tag;
    }
}
