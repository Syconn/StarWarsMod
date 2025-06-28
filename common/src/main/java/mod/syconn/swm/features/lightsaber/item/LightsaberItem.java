package mod.syconn.swm.features.lightsaber.item;

import mod.syconn.swm.core.ModComponents;
import mod.syconn.swm.features.lightsaber.data.LightsaberComponent;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import mod.syconn.swm.util.client.IItemExtended;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightsaberItem extends Item implements IItemExtended {

    public LightsaberItem() {
        super(new Properties().stacksTo(1).component(ModComponents.LIGHTSABER.get(), LightsaberComponent.create()).attributes(createAttributes(null)));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        LightsaberTag.update(stack, LightsaberTag::tick);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(stack));
    }

    @Override
    public boolean allowUpdateAnimation(@NotNull ItemStack from, @NotNull ItemStack to, boolean changed) {
        return LightsaberTag.identical(from, to);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        final var stack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    public static ItemAttributeModifiers createAttributes(@Nullable ItemStack stack) {
        var damage = stack != null && LightsaberComponent.get(stack).active() ? 7.0F : 0.0F;
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
