package mod.syconn.swm.features.lightsaber.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swm.addons.LightsaberContent;
import mod.syconn.swm.core.ModComponents;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.codec.StreamCodecs;
import mod.syconn.swm.util.math.Ease;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.UnaryOperator;

public record LightsaberComponent(int model, boolean stable, boolean active, byte transition, double lengthScalar, double radius, int color, List<Vec3> emitterPositions) {
    public static final StreamCodec<RegistryFriendlyByteBuf, LightsaberComponent> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public LightsaberComponent decode(RegistryFriendlyByteBuf buf) {
            return new LightsaberComponent(ByteBufCodecs.INT.decode(buf), ByteBufCodecs.BOOL.decode(buf), ByteBufCodecs.BOOL.decode(buf), ByteBufCodecs.BYTE.decode(buf),
                    ByteBufCodecs.DOUBLE.decode(buf), ByteBufCodecs.DOUBLE.decode(buf), ByteBufCodecs.INT.decode(buf), StreamCodecs.VEC3.apply(ByteBufCodecs.list()).decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, LightsaberComponent component) {
            ByteBufCodecs.INT.encode(buf, component.model);
            ByteBufCodecs.BOOL.encode(buf, component.stable);
            ByteBufCodecs.BOOL.encode(buf, component.active);
            ByteBufCodecs.BYTE.encode(buf, component.transition);
            ByteBufCodecs.DOUBLE.encode(buf, component.lengthScalar);
            ByteBufCodecs.DOUBLE.encode(buf, component.radius);
            ByteBufCodecs.INT.encode(buf, component.color);
            StreamCodecs.VEC3.apply(ByteBufCodecs.list()).encode(buf, component.emitterPositions);
        }
    };

    public static final Codec<LightsaberComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("model").forGetter(LightsaberComponent::model),
            Codec.BOOL.fieldOf("stable").forGetter(LightsaberComponent::stable),
            Codec.BOOL.fieldOf("active").forGetter(LightsaberComponent::active),
            Codec.BYTE.fieldOf("transition").forGetter(LightsaberComponent::transition),
            Codec.DOUBLE.fieldOf("lengthScalar").forGetter(LightsaberComponent::lengthScalar),
            Codec.DOUBLE.fieldOf("radius").forGetter(LightsaberComponent::radius),
            Codec.INT.fieldOf("color").forGetter(LightsaberComponent::color),
            Vec3.CODEC.listOf().fieldOf("emitterPositions").forGetter(LightsaberComponent::emitterPositions)
    ).apply(builder, LightsaberComponent::new));

    private static final byte TRANSITION_TICKS = 8;

    public static LightsaberComponent get(ItemStack stack) {
        return stack.getOrDefault(ModComponents.LIGHTSABER.get(), create());
    }

    public static LightsaberComponent create() {
        return LightsaberContent.LIGHTSABER_DATA.get(Constants.withId("mace")).orElse(new LightsaberData(0, true, 1.4f, 1, List.of())).component();
    }

    public static ItemStack update(ItemStack stack, UnaryOperator<LightsaberComponent> function) {
        stack.update(ModComponents.LIGHTSABER.get(), create(), function);
        return stack;
    }

    public static ItemStack set(ItemStack stack, LightsaberComponent component) {
        stack.set(ModComponents.LIGHTSABER.get(), component);
        return stack;
    }

    public float getSize(float partialTicks) {
        if (transition == 0) return active ? 1 : 0;
        if (transition > 0) return Ease.outCubic(1 - (transition - partialTicks) / TRANSITION_TICKS);
        return Ease.inCubic(-(transition + partialTicks) / TRANSITION_TICKS);
    }

    public LightsaberComponent toggle() {
        if (transition != 0) return this;
        var t = active ? -TRANSITION_TICKS : TRANSITION_TICKS;
        var a = !active;
        return new LightsaberComponent(model, stable, a, t, lengthScalar, radius, color, emitterPositions);
    }

    public LightsaberComponent tick() {
        var t = transition;
        if (transition > 0) t--;
        if (transition < 0) t++;
        return new LightsaberComponent(model, stable, active, t, lengthScalar, radius, color, emitterPositions);
    }

    public LightsaberComponent activation() {
        return new LightsaberComponent(model, stable, true, transition, lengthScalar, radius, color, emitterPositions);
    }
}
