package mod.syconn.swm.client;

import com.mojang.authlib.GameProfile;
import mod.syconn.swm.client.screen.HologramScreen;
import mod.syconn.swm.utils.block.WorldPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ClientHooks {

    public static Screen createHologramScreen(WorldPos worldPos, @Nullable ItemStack stack) {
        return new HologramScreen(worldPos, stack);
    }

    public static AbstractClientPlayer createMockPlayer(ClientLevel level, String name) {
        return new AbstractClientPlayer(level, new GameProfile(UUID.nameUUIDFromBytes(name.getBytes()), name)) {};
    }

    public static PlayerInfo getInfo(Player player) {
        return new PlayerInfo(player.getGameProfile(), false);
    }
}
