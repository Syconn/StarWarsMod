package mod.syconn.swm.util.json;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static <T> List<T> getArray(JsonObject json, Function<JsonObject, T> function) {
        List<T> list = new ArrayList<>();
        json.entrySet().forEach(e -> list.add(function.apply(e.getValue().getAsJsonObject())));
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

    public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
        var itemName = GsonHelper.getAsString(json, "item");
        var item = ShapedRecipe.itemFromJson(json);
        if (readNBT && json.has("nbt")) {
            var nbt = getNBT(json.get("nbt"));
            var tmp = new CompoundTag();
            tmp.put("tag", nbt);
            tmp.putString("id", itemName);
            var stack = new ItemStack(item);
            stack.setTag(nbt);
            return stack;
        }

        return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
    }

    public static CompoundTag getNBT(JsonElement element) {
        try {
            if (element.isJsonObject()) return TagParser.parseTag(GSON.toJson(element));
            else return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
        }
        catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + e);
        }
    }
}
