package mod.syconn.swm.features.blaster.item;

import mod.syconn.swm.features.blaster.BlasterUtil;
import mod.syconn.swm.features.blaster.server.data.BlasterTag;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.network.PlayAmbientLightsaberSoundPacket;
import mod.syconn.swm.features.lightsaber.server.data.LightsaberTag;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.utils.generic.ItemStackUtil;
import mod.syconn.swm.utils.interfaces.IItemExtensions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BlasterItem extends Item implements IItemExtensions {

    public BlasterItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)); // TODO change sound
        if (!level.isClientSide) {
            var bT = BlasterTag.getOrCreate(stack);

            var hS = (level.random.nextFloat() * 2 - 1) * bT.muzzle.accuracy;
            var vS = (level.random.nextFloat() * 2 - 1) * bT.muzzle.accuracy;

            BlasterTag.update(stack, t -> t.timeSinceLastShot = 0);

            BlasterUtil.fireBolt(level, player, bT.muzzle.maxRange, d -> (double) bT.muzzle.damage, false, entity -> {
                entity.shootFromRotation(player, player.getXRot() + hS, player.getYRot() + vS, 0.0F, 5.0F, 0.0F);
                entity.setPos(player.position().add(new Vec3(0, player.getEyeHeight() - entity.getBbHeight() / 2f, 0)));
                entity.setColor(bT.muzzle.color);

                entity.setLength(1); // TODO ALLOW MODIFY
                entity.setRadius(1);

                entity.setSourceArm(usedHand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite());

//                if (bt.getFiringMode() == BlasterFiringMode.SLUGTHROWER) entity.setSmoldering(true);
            });
//            return InteractionResultHolder.success(stack);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.pass(stack);
    }

    public static Hold getHold(LivingEntity entity) {
        if (entity == null) return Hold.NOT_HOLDING;

        var mainHand = entity.getMainHandItem();
        var offHand = entity.getOffhandItem();
        var hold = Hold.NOT_HOLDING;

        if (mainHand.getItem() instanceof BlasterItem) {
            var mainBd = BlasterTag.getOrCreate(mainHand);
            if (mainBd.muzzle.oneHanded) hold = Hold.ONE_HANDED_MAIN;
            else hold = Hold.TWO_HANDED_MAIN;
        }

        if (offHand.getItem() instanceof BlasterItem) {
            var offBd = BlasterTag.getOrCreate(offHand);
            if (offBd.muzzle.oneHanded) hold = hold == Hold.ONE_HANDED_MAIN ? Hold.DUAL : (hold == Hold.TWO_HANDED_MAIN ? Hold.NOT_HOLDING : Hold.ONE_HANDED_OFF);
            else hold = hold == Hold.NOT_HOLDING ? Hold.TWO_HANDED_OFF : Hold.NOT_HOLDING;
        }

        return hold;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) BlasterTag.update(stack, BlasterTag::tick);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack from, @NotNull ItemStack to, boolean changed) {
        return BlasterTag.identical(from, to);
    }

    public enum Hold {
        NOT_HOLDING(null, false, false, false),
        ONE_HANDED_MAIN(InteractionHand.MAIN_HAND, true, false, true),
        ONE_HANDED_OFF(InteractionHand.OFF_HAND, false, true, true),
        TWO_HANDED_MAIN(InteractionHand.MAIN_HAND, true, true, true),
        TWO_HANDED_OFF(InteractionHand.OFF_HAND, true, true, true),
        DUAL(InteractionHand.MAIN_HAND, true, true, false);

        private final InteractionHand defaultHand;
        public final boolean hasBlaster;
        public final boolean mainHandOccupied;
        public final boolean offHandOccupied;
        public final boolean oneWeapon;

        Hold(InteractionHand defaultHand, boolean mainHandOccupied, boolean offHandOccupied, boolean oneWeapon) {
            this.hasBlaster = defaultHand != null;
            this.defaultHand = defaultHand;
            this.mainHandOccupied = mainHandOccupied;
            this.offHandOccupied = offHandOccupied;
            this.oneWeapon = oneWeapon;
        }

        public boolean isDefaultHand(InteractionHand hand) {
            if (hand == null) return false;
            return this.defaultHand == hand;
        }

        public boolean twoHanded() {
            return mainHandOccupied && offHandOccupied;
        }
    }

}
