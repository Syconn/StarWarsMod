package mod.syconn.swm.util.json;

import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JsonUtils {

    public static <T> List<T> getArray(JsonObject json, Function<JsonObject, T> function) {
        var list = new ArrayList<T>();
        json.asMap().forEach((key, value) -> list.add(function.apply(value.getAsJsonObject())));
        return list;
    }

    public static <T> JsonObject addArray(List<T> elements, Function<T, JsonObject> function) {
        var json = new JsonObject();
        for (var i = 0; i < elements.size(); i++) json.add(String.valueOf(i), function.apply(elements.get(i)));
        return json;
    }

    public static JsonObject addVec3(Vec3 vec3) {
        var json = new JsonObject();
        json.addProperty("x", vec3.x);
        json.addProperty("y", vec3.y);
        json.addProperty("z", vec3.z);
        return json;
    }

    public static Vec3 getVec3(JsonObject json) {
        var x = json.get("x").getAsDouble();
        var y = json.get("y").getAsDouble();
        var z = json.get("z").getAsDouble();
        return new Vec3(x, y, z);
    }
}
