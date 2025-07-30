package mod.syconn.swm.utils.generic;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.MathHelper;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.PlayAnimationPacket;
import mod.syconn.swm.utils.Constants;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;
import java.util.Optional;

public class AnimationUtil {

    public static final AnimationSubStack<IAnimation> SW_PLAYER_STACK = new AnimationSubStack<>();

    public static float inCubic(float t) {
        return t * t * t;
    }

    public static float outCubic(float t) {
        t--;
        return (t * t * t + 1);
    }

    public static float inOutCubic(float t) {
        t *= 2;
        if (t < 1) return t * t * t / 2;
        t -= 2;
        return (t * t * t + 2) / 2;
    }

    @SuppressWarnings("unchecked")
    public static Optional<ModifierLayer<IAnimation>> getAnimatorLayer(AbstractClientPlayer player) {
        var anim = PlayerAnimationAccess.getPlayerAssociatedData(player).get(Constants.withId("player_animations"));
        return anim instanceof ModifierLayer ? Optional.of((ModifierLayer<IAnimation>) anim) : Optional.empty();
    }

    public static AnimationStack getAnimationStack(AbstractClientPlayer clientPlayer) {
        return PlayerAnimationAccess.getPlayerAnimLayer(clientPlayer);
    }

    public static KeyframeAnimation getAnimation(String name) {
        return PlayerAnimationRegistry.getAnimation(Constants.withId(name));
    }

//    public static void play(AbstractClientPlayer player, String animation, int length, Ease ease) { TODO OLD SYSTEM
//        AnimationUtil.getAnimatorLayer(player).ifPresent(layer -> layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(length, ease), new KeyframeAnimationPlayer(getAnimation(animation))));
//    }

    public static void play(AbstractClientPlayer player, String name, int fadeIn, Ease ease) {
        KeyframeAnimation animation = getAnimation(name);
        var mirror = player.getMainArm() == HumanoidArm.LEFT;
        SW_PLAYER_STACK.mirror.setEnabled(mirror);
        SW_PLAYER_STACK.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeIn, ease), new KeyframeAnimationPlayer(animation));
    }

    public static void notifyPlayers(ServerPlayer serverPlayer, String animation, int length, Ease ease) {
        serverPlayer.serverLevel().players().forEach(p -> Network.CHANNEL.sendToPlayer(p, new PlayAnimationPacket(serverPlayer.getUUID(), animation, length, ease)));
    }

    public static class AnimationSubStack<T extends IAnimation> {
        public final SpeedModifier speed = new SpeedModifier();
        public final MirrorModifier mirror = new MirrorModifier();
        public final ModifierLayer<T> base = new ModifierLayer<>();

        public AnimationSubStack() {
            mirror.setEnabled(false);
            base.addModifier(speed, 0);
            base.addModifier(mirror, 0);
        }
    }
}
