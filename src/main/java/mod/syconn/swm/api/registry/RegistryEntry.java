package mod.syconn.swm.api.registry;

import mod.syconn.swm.api.services.Registration;
import mod.syconn.swm.utils.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryEntry<T> {

    protected final ResourceKey<Registry<T>> registry;
    protected final ResourceLocation id;
    protected final Supplier<T> supplier;

    public RegistryEntry(Registry<?> registry, ResourceLocation id, Supplier<T> supplier) {
        this.registry = ResourceKey.createRegistryKey(registry.key().location());
        this.id = id;
        this.supplier = supplier;
        this.tabbed = true;
    }

    protected boolean tabbed;
    private T instance;

    public T get() {
        if(this.instance == null) throw new IllegalStateException("Entry has not been created yet");
        return this.instance;
    }

    protected T create() {
        if(this.instance != null) throw new IllegalStateException("Entry has already been created");
        this.instance = this.supplier.get();
        return this.instance;
    }

    public ResourceKey<Registry<T>> getRegistry() {
        return registry;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    protected void invalidate() {
        this.instance = null;
    }

    public RegistryEntry<T> setTabbed(boolean tabbed) {
        this.tabbed = tabbed;
        return this;
    }

    public void register(RegisterConsumer<T> consumer) {
        this.invalidate();
        T value = this.create();
        consumer.accept(this.registry, this.id, () -> value);
    }

    public static <T extends Item> RegistryEntry<T> item(String id, Function<Item.Properties, T> itemFactory) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), () -> {
            var itemProperties = new Item.Properties();
            //? >1.21.11
            //itemProperties.setId(ResourceKey.create(Registries.ITEM, Constants.withId(id)));
            return itemFactory.apply(itemProperties);
        });
    }

    public static <T extends Item> RegistryEntry<T> item(String id, Function<Item.Properties, T> itemFactory, Supplier<Item.Properties> itemPropertiesFactory) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), () -> {
            var itemProperties = itemPropertiesFactory.get();
            //? >1.21.11
            //itemProperties.setId(ResourceKey.create(Registries.ITEM, Constants.withId(id)));
            return itemFactory.apply(itemProperties);
        });
    }

    public static RegistryEntry<Item> item(String id, Item.Properties properties) {
        return new RegistryEntry<>(BuiltInRegistries.ITEM, Constants.withId(id), () -> {
            //? >1.21.11
            //properties.setId(ResourceKey.create(Registries.ITEM, Constants.withId(id)));
            return new Item(properties);
        });
    }

    public static RegistryEntry<CreativeModeTab> creativeModeTab(String id, Consumer<CreativeModeTab.Builder> builderConsumer) {
        return new RegistryEntry<>(BuiltInRegistries.CREATIVE_MODE_TAB, Constants.withId(id), () -> {
            var builder = Registration.createCreativeModeTabBuilder();
            builderConsumer.accept(builder);
            return builder.build();
        });
    }

    public static <T extends Block> RegistryEntry<T> block(String id, Supplier<T> supplier) {
        return new BlockRegistryEntry<>(BuiltInRegistries.BLOCK, Constants.withId(id), supplier, t -> null);
    }

    public static <T extends Block, E extends BlockItem> RegistryEntry<T> blockWithItem(String id, Supplier<T> supplier) {
        return new BlockRegistryEntry<>(BuiltInRegistries.BLOCK, Constants.withId(id), supplier, t -> new BlockItem(t, new Item.Properties()));
    }

    public static <T extends Fluid> RegistryEntry<T> fluid(String id, Supplier<T> fluidFactory) {
        return new RegistryEntry<>(BuiltInRegistries.FLUID, Constants.withId(id), fluidFactory);
    }

    public static <T extends BlockEntity> RegistryEntry<BlockEntityType<T>> blockEntity(String id, BiFunction<BlockPos, BlockState, T> blockEntityFactory, Supplier<Block> validBlock) {
        return new RegistryEntry<>(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.withId(id), () -> Registration.createBlockEntityType(blockEntityFactory, () -> new Block[]{ validBlock.get() }));
    }

    public static <T extends BlockEntity> RegistryEntry<BlockEntityType<T>> blockEntities(String id, BiFunction<BlockPos, BlockState, T> blockEntityFactory, Supplier<Block[]> validBlock) {
        return new RegistryEntry<>(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.withId(id), () -> Registration.createBlockEntityType(blockEntityFactory, validBlock));
    }

    public static <T extends EntityType<?>> RegistryEntry<T> entityType(String id, Supplier<T> supplier) {
        return new RegistryEntry<>(BuiltInRegistries.ENTITY_TYPE, Constants.withId(id), supplier);
    }

    public static <T extends AbstractContainerMenu> RegistryEntry<MenuType<T>> menuType(String id, BiFunction<Integer, Inventory, T> function) {
        return new RegistryEntry<>(BuiltInRegistries.MENU, Constants.withId(id), () -> Registration.createMenuType(function));
    }

    public static <T extends AbstractContainerMenu> RegistryEntry<MenuType<T>> menuTypeWithData(String id, TriFunction<Integer, Inventory, FriendlyByteBuf, T> function) {
        return new RegistryEntry<>(BuiltInRegistries.MENU, Constants.withId(id), () -> Registration.createMenuTypeWithData(function));
    }

    public static <T extends ParticleType<?>> RegistryEntry<T> particleType(String id, Supplier<T> particleTypeFactory) {
        return new RegistryEntry<>(BuiltInRegistries.PARTICLE_TYPE, Constants.withId(id), particleTypeFactory);
    }

    public static <T extends Recipe<?>> RegistryEntry<RecipeType<T>> recipeType(String id) {
        return new RegistryEntry<>(BuiltInRegistries.RECIPE_TYPE, Constants.withId(id), () -> new RecipeType<T>(id) {
            public String toString() {
                return id;
            }
        });
    }

    public static <T extends RecipeSerializer<?>> RegistryEntry<T> recipeSerializer(String id, Supplier<T> recipeSerializerFactory) {
        return new RegistryEntry<>(BuiltInRegistries.RECIPE_SERIALIZER, Constants.withId(id), recipeSerializerFactory);
    }

    public static <T extends SoundEvent> RegistryEntry<T> soundEvent(String id, Function<ResourceLocation, Supplier<T>> soundEventFactory) {
        return new RegistryEntry<>(BuiltInRegistries.SOUND_EVENT, Constants.withId(id), soundEventFactory.apply(Constants.withId(id)));
    }

    public static <T extends SoundEvent> RegistryEntry<SoundEvent> soundEvent(String id) {
        return new RegistryEntry<>(BuiltInRegistries.SOUND_EVENT, Constants.withId(id), () -> SoundEvent.createVariableRangeEvent(Constants.withId(id)));
    }
}
