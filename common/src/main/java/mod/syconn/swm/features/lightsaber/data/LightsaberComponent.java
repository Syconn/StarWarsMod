package mod.syconn.swm.features.lightsaber.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swm.client.StarWarsClient;
import mod.syconn.swm.core.ModComponents;
import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.client.model.NodeVec3;
import mod.syconn.swm.util.codec.StreamCodecs;
import mod.syconn.swm.util.math.Ease;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public record LightsaberComponent(UUID uuid, int model, boolean stable, float lengthScalar, boolean active, byte transition, double radius, int color, String bladeType, List<NodeVec3> emitterPositions) {
    public static final StreamCodec<RegistryFriendlyByteBuf, LightsaberComponent> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull LightsaberComponent decode(RegistryFriendlyByteBuf buf) {
            return new LightsaberComponent(UUIDUtil.STREAM_CODEC.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.BOOL.decode(buf), ByteBufCodecs.FLOAT.decode(buf),
                    ByteBufCodecs.BOOL.decode(buf), ByteBufCodecs.BYTE.decode(buf), ByteBufCodecs.DOUBLE.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.STRING_UTF8.decode(buf),
                    StreamCodecs.NODE_VEC3.apply(ByteBufCodecs.list()).decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, LightsaberComponent component) {
            UUIDUtil.STREAM_CODEC.encode(buf, component.uuid);
            ByteBufCodecs.INT.encode(buf, component.model);
            ByteBufCodecs.BOOL.encode(buf, component.stable);
            ByteBufCodecs.FLOAT.encode(buf, component.lengthScalar);
            ByteBufCodecs.BOOL.encode(buf, component.active);
            ByteBufCodecs.BYTE.encode(buf, component.transition);
            ByteBufCodecs.DOUBLE.encode(buf, component.radius);
            ByteBufCodecs.INT.encode(buf, component.color);
            ByteBufCodecs.STRING_UTF8.encode(buf, component.bladeType);
            StreamCodecs.NODE_VEC3.apply(ByteBufCodecs.list()).encode(buf, component.emitterPositions);
        }
    };

    public static final Codec<LightsaberComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(LightsaberComponent::uuid), Codec.INT.fieldOf("model").forGetter(LightsaberComponent::model),
            Codec.BOOL.fieldOf("stable").forGetter(LightsaberComponent::stable), Codec.FLOAT.fieldOf("lengthScalar").forGetter(LightsaberComponent::lengthScalar),
            Codec.BOOL.fieldOf("active").forGetter(LightsaberComponent::active), Codec.BYTE.fieldOf("transition").forGetter(LightsaberComponent::transition),
            Codec.DOUBLE.fieldOf("radius").forGetter(LightsaberComponent::radius), Codec.INT.fieldOf("color").forGetter(LightsaberComponent::color),
            Codec.STRING.fieldOf("bladeType").forGetter(LightsaberComponent::bladeType), NodeVec3.NODE_VEC3_CODEC.listOf().fieldOf("emitterPositions").forGetter(LightsaberComponent::emitterPositions)
    ).apply(builder, LightsaberComponent::new));

    private static final byte TRANSITION_TICKS = 8;

    public static LightsaberComponent getOrCreate(ItemStack stack) {
        return stack.getOrDefault(ModComponents.LIGHTSABER.get(), create());
    }

    public static LightsaberComponent create() {
        return LightsaberContent.LIGHTSABER_DATA.get(Constants.withId("mace")).orElse(new LightsaberData(0, true, 1.4f, 1, LightsaberContent.GREEN, LightsaberContent.PLASMA, List.of())).component();
    }

    public static ItemStack update(ItemStack stack, UnaryOperator<LightsaberComponent> function) {
        stack.update(ModComponents.LIGHTSABER.get(), create(), function);
        return stack;
    }

    public static ItemStack set(ItemStack stack, LightsaberComponent component) {
        stack.set(ModComponents.LIGHTSABER.get(), component);
        return stack;
    }

    public static boolean identical(ItemStack stack1, ItemStack stack2) {
        return LightsaberComponent.getOrCreate(stack1).uuid.equals(LightsaberComponent.getOrCreate(stack2).uuid);
    }

    public float getSize() {
        var partialTicks = StarWarsClient.getTickDelta();
        if (transition == 0) return active ? 1 : 0;
        if (transition > 0) return Ease.outCubic(1 - (transition - partialTicks) / TRANSITION_TICKS);
        return Ease.inCubic(-(transition + partialTicks) / TRANSITION_TICKS);
    }

    public ItemStack getTemporary(boolean active, boolean singleBlade) {
        var emitters = singleBlade ? List.of(this.emitterPositions.get(0)) : this.emitterPositions;
        return set(new ItemStack(ModItems.LIGHTSABER.get()), new LightsaberComponent(this.uuid, this.model, this.stable, this.lengthScalar, active, this.transition, this.radius, this.color, this.bladeType, emitters));
    }

    public ItemStack getTemporary(LightsaberComponent original, boolean active) {
        return set(new ItemStack(ModItems.LIGHTSABER.get()), original.active(active));
    }

    public LightsaberComponent toggle() {
        if (transition != 0) return this;
        var t = active ? -TRANSITION_TICKS : TRANSITION_TICKS;
        var a = !active;
        return new LightsaberComponent(uuid, model, stable, lengthScalar, a, t, radius, color, bladeType, emitterPositions);
    }

    public LightsaberComponent tick() {
        var t = transition;
        if (transition > 0) t--;
        if (transition < 0) t++;
        return new LightsaberComponent(uuid, model, stable, lengthScalar, active, t, radius, color, bladeType, emitterPositions);
    }

    public LightsaberComponent active(boolean active) {
        return new LightsaberComponent(uuid, model, stable, lengthScalar, active, transition, radius, color, bladeType, emitterPositions);
    }

    public LightsaberComponent activate() {
        return active(true);
    }

    public LightsaberComponent color(int hsv) {
        return new LightsaberComponent(uuid, model, stable, lengthScalar, active, transition, radius, hsv, bladeType, emitterPositions);
    }
}
