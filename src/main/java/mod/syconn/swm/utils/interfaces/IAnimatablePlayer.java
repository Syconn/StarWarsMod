package mod.syconn.swm.utils.interfaces;

import dev.kosmx.playerAnim.core.util.Ease;

public interface IAnimatablePlayer { // TODO MIGRATE TO PLAYERANIMATOR LIBRARY for 1.21+

    default void swm$playAnimation(String name, int fadeIn, Ease ease) {}
    default void swm$stopAnimation(int fadeOut, Ease ease) {}
}
