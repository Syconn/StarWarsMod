package mod.syconn.swm.features.blaster.server.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.features.addons.BlasterContent;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class MuzzleData {

    public String projectileType;
    public int color;
    public int heat;
    public int heatTolerance;
    public float heatDispatchTime;
    public float maxRange;
    public float fireRate;
    public float damage;
    public float accuracy;
    public boolean oneHanded;
    public Holster holster;

    public MuzzleData(String projectileType, int color, int heat, int heatTolerance, float heatDispatchTime, float maxRange, float fireRate, float damage, float accuracy, boolean oneHanded, Holster holster) {
        this.projectileType = projectileType;
        this.color = color;
        this.heat = heat;
        this.heatTolerance = heatTolerance;
        this.heatDispatchTime = heatDispatchTime;
        this.maxRange = maxRange;
        this.fireRate = fireRate;
        this.damage = damage;
        this.accuracy = accuracy;
        this.oneHanded = oneHanded;
        this.holster = holster;
    }

    public MuzzleData(CompoundTag tag) {
        this.projectileType = tag.getString("projectileType");
        this.color = tag.getInt("color");
        this.heat = tag.getInt("heat");
        this.heatTolerance = tag.getInt("heatTolerance");
        this.heatDispatchTime = tag.getFloat("heatDispatchTime");
        this.maxRange = tag.getFloat("maxRange");
        this.fireRate = tag.getFloat("fireRate");
        this.damage = tag.getFloat("damage");
        this.accuracy = tag.getFloat("accuracy");
        this.oneHanded = tag.getBoolean("oneHanded");
        this.holster = NBTUtil.getEnum(Holster.class, tag.getCompound("holster"));
    }

    public MuzzleData(JsonObject json) {
        this.projectileType = json.get("projectileType").getAsString();
        this.color = json.get("color").getAsInt();
        this.heat = json.get("heat").getAsInt();
        this.heatTolerance = json.get("heatTolerance").getAsInt();
        this.heatDispatchTime = json.get("heatDispatchTime").getAsFloat();
        this.maxRange = json.get("maxRange").getAsFloat();
        this.fireRate = json.get("fireRate").getAsFloat();
        this.damage = json.get("damage").getAsFloat();
        this.accuracy = json.get("accuracy").getAsFloat();
        this.oneHanded = json.get("oneHanded").getAsBoolean();
        this.holster = Holster.valueOf(json.get("holster").getAsString());
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putString("projectileType", this.projectileType);
        tag.putInt("color", this.color);
        tag.putInt("heat", this.heat);
        tag.putInt("heatTolerance", this.heatTolerance);
        tag.putFloat("heatDispatchTime", this.heatDispatchTime);
        tag.putFloat("maxRange", this.maxRange);
        tag.putFloat("fireRate", this.fireRate);
        tag.putFloat("damage", this.damage);
        tag.putFloat("accuracy", this.accuracy);
        tag.putBoolean("oneHanded", this.oneHanded);
        tag.put("holster", NBTUtil.putEnum(this.holster));
        return tag;
    }

    public JsonObject json() {
        JsonObject json = new JsonObject();
        json.addProperty("projectileType", this.projectileType);
        json.addProperty("color", this.color);
        json.addProperty("heat", this.heat);
        json.addProperty("heatTolerance", this.heatTolerance);
        json.addProperty("heatDispatchTime", this.heatDispatchTime);
        json.addProperty("maxRange", this.maxRange);
        json.addProperty("fireRate", this.fireRate);
        json.addProperty("damage", this.damage);
        json.addProperty("accuracy", this.accuracy);
        json.addProperty("oneHanded", this.oneHanded);
        json.addProperty("holster", this.holster.name());
        return json;
    }

    public enum Holster {
        SIDEARM,
        BACK
    }
}
