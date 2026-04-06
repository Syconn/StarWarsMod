package mod.syconn.swm.features.lightsaber.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.utils.Config;
import mod.syconn.swm.utils.client.SoundHelper;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import mod.syconn.swm.utils.interfaces.IItemExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LightsaberItem extends Item implements IItemExtensions, IEquipmentItem {

    public LightsaberItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && isSelected) LightsaberTag.update(stack, LightsaberTag::tick);
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack from, @NotNull ItemStack to, boolean changed) {
        return LightsaberTag.identical(from, to);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        final var stack = player.getItemInHand(usedHand);
        if (LightsaberTag.getOrCreate(stack).isActive()) player.startUsingItem(usedHand);
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifications(ItemStack stack, EquipmentSlot slot) {
        var damage = LightsaberTag.getOrCreate(stack).isActive() ? Config.SERVER.baseLightsaberDamage : 0.5f;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public SWEquipmentSlot getSWEquipmentSlot() {
        return SWEquipmentSlot.LIGHTSABER;
    }

    @Override
    public boolean onItemDeselected(Player player, ItemStack stack) {
        LightsaberTag.update(stack, tag -> {
            if (!player.level().isClientSide && tag.isActive()) {
                tag.quickTurnoff();
                SoundHelper.playToggleAudio(player.level(), player.blockPosition(), false);
            }
        });
        return true;
    }
}
