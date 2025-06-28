package mod.syconn.swm.client.keys;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.core.ModKeys;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.Network;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class KeyHandler {

    public static void handleKeyMappings(Player player) {
        while (ModKeys.TOGGLE_ITEM.consumeClick()) {
            InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (player.getItemInHand(hand).getItem() instanceof LightsaberItem) NetworkManager.sendToServer(new ToggleLightsaberPacket(hand));
        }

        while (ModKeys.POWER_1.consumeClick()) {
            InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (player.getItemInHand(hand).getItem() instanceof LightsaberItem) NetworkManager.sendToServer(new ThrowLightsaberPacket(hand));
        }
    }
}
