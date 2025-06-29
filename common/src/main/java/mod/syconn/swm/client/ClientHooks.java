package mod.syconn.swm.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.UUIDUtil;

public class ClientHooks {

    public static AbstractClientPlayer fakePlayer(ClientLevel level, String username) {
        return new AbstractClientPlayer(level, new GameProfile(UUIDUtil.createOfflinePlayerUUID(username), "Hologram-" + username)) {

        };
    }
}
