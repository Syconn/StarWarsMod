package mod.syconn.swm.util.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record StackedIngredient(Ingredient ingredient, int count) {
    public static final StackedIngredient EMPTY = new StackedIngredient(Ingredient.EMPTY, 0);
    public static final Codec<StackedIngredient> CODEC = RecordCodecBuilder.create(builder -> builder.group(Ingredient.CODEC_NONEMPTY.fieldOf("ingredient")
            .forGetter(o -> o.ingredient), Codec.INT.fieldOf("count").orElse(1).forGetter(o -> o.count)).apply(builder, StackedIngredient::new));

    public static StackedIngredient fromNetwork(RegistryFriendlyByteBuf buf) {
        return new StackedIngredient(Ingredient.CONTENTS_STREAM_CODEC.decode(buf), buf.readInt());
    }

    public void toNetwork(RegistryFriendlyByteBuf buf) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, this.ingredient);
        buf.writeInt(this.count);
    }

    public static StackedIngredient of(TagKey<Item> tag, int count) {
        return new StackedIngredient(Ingredient.of(tag), count);
    }

    public static StackedIngredient of(ItemStack stack) {
        return new StackedIngredient(Ingredient.of(stack), stack.getCount());
    }

    public static StackedIngredient of(Ingredient ingredient, int count) {
        return new StackedIngredient(ingredient, count);
    }
}