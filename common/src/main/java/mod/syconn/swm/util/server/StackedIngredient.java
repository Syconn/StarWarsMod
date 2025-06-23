package mod.syconn.swm.util.server;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class StackedIngredient extends Ingredient {
    private final Value itemList;
    private final int count;

    protected StackedIngredient(Stream<? extends Value> itemList, int count) {
        super(itemList);
        this.itemList = null;
        this.count = count;
    }

    private StackedIngredient(Value itemList, int count) {
        super(Stream.of(itemList));
        this.itemList = itemList;
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public static StackedIngredient fromJson(JsonObject object) {
        Ingredient.Value value = valueFromJson(object);
        int count = GsonHelper.getAsInt(object, "count", 1);
        return new StackedIngredient(Stream.of(value), count);
    }

    public JsonElement toJson() {
        JsonObject object = this.itemList.serialize();
        object.addProperty("count", this.count);
        return object;
    }

    public static StackedIngredient of(ItemLike provider, int count) {
        return new StackedIngredient(new ItemValue(new ItemStack(provider)), count);
    }

    public static StackedIngredient of(ItemStack stack, int count) {
        return new StackedIngredient(new ItemValue(stack), count);
    }

    public static StackedIngredient of(TagKey<Item> tag, int count) {
        return new StackedIngredient(new TagValue(tag), count);
    }

    private static Ingredient.Value valueFromJson(JsonObject json) {
        if (json.has("item") && json.has("tag")) {
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        } else if (json.has("item")) {
            Item item = ShapedRecipe.itemFromJson(json);
            return new ItemValue(new ItemStack(item));
        } else if (json.has("tag")) {
            ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
            TagKey<Item> tagKey = TagKey.create(Registries.ITEM, resourceLocation);
            return new TagValue(tagKey);
        } else {
            throw new JsonParseException("An ingredient entry needs either a tag or an item");
        }
    }

    private record ItemValue(ItemStack item) implements Value {

        @Override
        public Collection<ItemStack> getItems() {
            return Collections.singleton(this.item);
        }

        @Override
        public JsonObject serialize() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item.getItem()).toString());
            return jsonObject;
        }
    }

    private record TagValue(TagKey<Item> tag) implements Value {

        @Override
        public Collection<ItemStack> getItems() {
            List<ItemStack> list = Lists.<ItemStack>newArrayList();

            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
                list.add(new ItemStack(holder));
            }

            return list;
        }

        @Override
        public JsonObject serialize() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tag", this.tag.location().toString());
            return jsonObject;
        }
    }
}
