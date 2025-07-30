package mod.syconn.swm.utils.generic;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.PlayAnimationPacket;
import mod.syconn.swm.utils.Constants;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class AnimationUtil {

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

    public static KeyframeAnimation getAnimation(String animation) {
        return PlayerAnimationRegistry.getAnimation(Constants.withId(animation));
    }

    public static void play(AbstractClientPlayer player, String animation, int length, Ease ease) {
        AnimationUtil.getAnimatorLayer(player).ifPresent(layer -> layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(length, ease), new KeyframeAnimationPlayer(getAnimation(animation))));
    }

    public static void notifyPlayers(ServerPlayer serverPlayer, String animation, int length, Ease ease) {
        serverPlayer.serverLevel().players().forEach(p -> Network.CHANNEL.sendToPlayer(p, new PlayAnimationPacket(serverPlayer.getUUID(), animation, length, ease)));
    }
}
