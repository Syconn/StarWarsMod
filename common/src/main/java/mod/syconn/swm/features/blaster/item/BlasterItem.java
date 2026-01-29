package mod.syconn.swm.features.blaster.item;

import mod.syconn.swm.core.ModItems;
import mod.syconn.swm.features.blaster.BlasterUtil;
import mod.syconn.swm.features.blaster.server.data.BlasterTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlasterItem extends Item {

    public BlasterItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            var bT = BlasterTag.getOrCreate(stack);

            var hS = (level.random.nextFloat() * 2 - 1) * bT.muzzle.accuracy;
            var vS = (level.random.nextFloat() * 2 - 1) * bT.muzzle.accuracy;

            BlasterUtil.fireBolt(level, player, bT.muzzle.maxRange, distance -> (double) bT.muzzle.damage, false, entity -> {
                entity.shootFromRotation(player, player.getXRot() + hS, player.getYRot() + vS, 0.0F, 5.0F, 0.0F);
                entity.setPos(player.position().add(new Vec3(0, player.getEyeHeight() - entity.getBbHeight() / 2f, 0)));
                entity.setColor(bT.muzzle.color);

                entity.setLength(1); // TODO ALLOW MODIFY
                entity.setRadius(1);

                entity.setSourceArm(usedHand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite());

//                if (bt.getFiringMode() == BlasterFiringMode.SLUGTHROWER) entity.setSmoldering(true);
            });
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.success(stack);
    }
}
