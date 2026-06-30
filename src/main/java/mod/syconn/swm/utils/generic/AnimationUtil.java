package mod.syconn.swm.utils.generic;

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

//    public static KeyframeAnimation getAnimation(String name) { TODO ANIMATION API
//        return PlayerAnimationRegistry.getAnimation(Constants.withId(name));
//    }
//
//    public static void notifyPlayers(ServerPlayer serverPlayer, String animation, int length, Ease ease) {
//        Network.CHANNEL.sendToPlayers(serverPlayer.serverLevel().players(), new PlayAnimationPacket(serverPlayer.getUUID(), animation, length, ease));
//    }
}
