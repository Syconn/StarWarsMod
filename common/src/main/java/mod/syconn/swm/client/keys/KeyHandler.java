package mod.syconn.swm.client.keys;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.core.ModKeys;
import mod.syconn.swm.features.lightsaber.item.LightsaberItem;
import mod.syconn.swm.features.lightsaber.network.ThrowLightsaberPacket;
import mod.syconn.swm.features.lightsaber.network.ToggleLightsaberPacket;
import mod.syconn.swm.network.Network;
import mod.syconn.swm.network.packets.serverside.ToggleEquipmentSlotPacket;
import mod.syconn.swm.server.data.SWGear;
import mod.syconn.swm.utils.interfaces.IEquipmentItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class KeyHandler {

    public static void handleKeyMappings(Player player) {
        while (ModKeys.TOGGLE_ITEM.consumeClick()) {
            InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (player.getItemInHand(hand).getItem() instanceof LightsaberItem) Network.CHANNEL.sendToServer(new ToggleLightsaberPacket(hand));
        }

        while (ModKeys.THROW_LIGHTSABER.consumeClick()) {
            InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LightsaberItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (player.getItemInHand(hand).getItem() instanceof LightsaberItem) Network.CHANNEL.sendToServer(new ThrowLightsaberPacket(hand));
        }

        while (ModKeys.QUICK_SWAP_LIGHTSABER.consumeClick()) {
            if (GameInstance.getClient().screen == null) {
                final var slot = findOpenHotbarSlot(player);
                final var equipment = IEquipmentItem.SWEquipmentSlot.LIGHTSABER;
                final var gear = ((SWGear.SWGearAccess) player).swm$getSWGear();
                Network.CHANNEL.sendToServer(new ToggleEquipmentSlotPacket(slot, player.getInventory().selected, equipment));
                if (!gear.getItemFromSlot(equipment).isEmpty() && !gear.getItemFromSlot(equipment).isEmpty() && slot != -1) player.getInventory().selected = slot;
            }
        }
    }

    private static int findOpenHotbarSlot(Player player) {
        if (player.getInventory().getItem(player.getInventory().selected).isEmpty()) return player.getInventory().selected;
        for (int slot = 0; slot < 9; slot++) if (player.getInventory().getItem(slot).isEmpty()) return slot;
        return -1;
    }
}
