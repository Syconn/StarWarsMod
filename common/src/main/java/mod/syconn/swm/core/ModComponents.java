package mod.syconn.swm.core;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

import static mod.syconn.swm.util.Constants.MOD;

public class ModComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(MOD, Registries.DATA_COMPONENT_TYPE);

    public static final Supplier<DataComponentType<LightsaberComponent>> LIGHTSABER = registerComponent("lightsaber", LightsaberComponent.CODEC, LightsaberComponent.STREAM_CODEC);

    private static <T> Supplier<DataComponentType<T>> registerComponent(String id, Codec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return DATA_COMPONENTS.register(id, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }
}
