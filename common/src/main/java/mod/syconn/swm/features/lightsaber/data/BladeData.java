package mod.syconn.swm.features.lightsaber.data;

import com.google.gson.JsonObject;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.utils.client.NodeVec3;
import mod.syconn.swm.utils.generic.AnimationUtil;
import net.minecraft.nbt.CompoundTag;

public class BladeData {

    private static final byte IGNITION_TICKS = 6;
    private static final byte RETRACTION_TICKS = 12;

    public boolean stable;
    public boolean active;
    public float bladeLengthScalar;
    public byte transition;
    public double radius;
    public int color;
    public String bladeType;
    public NodeVec3 emitterPos;

    public BladeData(boolean stable, boolean active, float bladeLengthScalar, byte transition, double radius, int color, String bladeType, NodeVec3 emitterPos) {
        this.stable = stable;
        this.active = active;
        this.bladeLengthScalar = bladeLengthScalar;
        this.transition = transition;
        this.radius = radius;
        this.color = color;
        this.bladeType = bladeType;
        this.emitterPos = emitterPos;
    }

    public BladeData(CompoundTag tag) {
        this.stable = !tag.contains("stable") || tag.getBoolean("stable");
        this.active = tag.contains("active") && tag.getBoolean("active");
        this.bladeLengthScalar = tag.contains("bladeLengthScalar") ? tag.getFloat("bladeLengthScalar") : 1.0f;
        this.transition = tag.contains("transition") ? tag.getByte("transition") : 0;
        this.radius = tag.contains("radius") ? tag.getDouble("radius") : 1d;
        this.color = tag.contains("color") ? tag.getInt("color") : LightsaberContent.BLUE;
        this.bladeType = tag.contains("bladeType") ? tag.getString("bladeType") : LightsaberContent.PLASMA;
        this.emitterPos = tag.contains("emitterPos") ? NodeVec3.getNode(tag.getCompound("emitterPos")) : new NodeVec3();
    }

    public BladeData(JsonObject json) {
        this.stable = json.get("stable").getAsBoolean();
        this.active = json.get("active").getAsBoolean();
        this.bladeLengthScalar = json.get("bladeLengthScalar").getAsFloat();
        this.transition = json.get("transition").getAsByte();
        this.radius = json.get("radius").getAsDouble();
        this.color = json.get("color").getAsInt();
        this.bladeType = json.get("bladeType").getAsString();
        this.emitterPos = NodeVec3.getNode(json.get("emitterPos").getAsJsonObject());
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putBoolean("stable", this.stable);
        tag.putBoolean("active", this.active);
        tag.putFloat("bladeLengthScalar", this.bladeLengthScalar);
        tag.putByte("transition", this.transition);
        tag.putDouble("radius", this.radius);
        tag.putInt("color", this.color);
        tag.putString("bladeType", this.bladeType);
        tag.put("emitterPos", this.emitterPos.save());
        return tag;
    }

    public JsonObject json() {
        JsonObject json = new JsonObject();
        json.addProperty("stable", this.stable);
        json.addProperty("active", this.active);
        json.addProperty("bladeLengthScalar", this.bladeLengthScalar);
        json.addProperty("transition", this.transition);
        json.addProperty("radius", this.radius);
        json.addProperty("color", this.color);
        json.addProperty("bladeType", this.bladeType);
        json.add("emitterPos", NodeVec3.addNode(this.emitterPos));
        return json;
    }

    public void toggle(boolean active) {
        if (this.active == active) this.toggle();
    }

    public void toggle() {
        if (this.transition != 0) return;
        this.transition = this.active ? -RETRACTION_TICKS : IGNITION_TICKS;
        this.active = !this.active;
    }

    public void tick() {
        if (this.transition > 0) this.transition--;
        if (this.transition < 0) this.transition++;
    }

    public float getSize() {
        var partialTicks = StarWarsClient.getTickDelta();
        if (this.transition == 0) return this.active ? 1 : 0;
        if (this.transition > 0) return AnimationUtil.outCubic(1 - (this.transition - partialTicks) / IGNITION_TICKS);
        return AnimationUtil.inCubic(-(this.transition + partialTicks) / RETRACTION_TICKS);
    }
}
