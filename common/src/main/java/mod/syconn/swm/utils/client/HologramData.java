package mod.syconn.swm.utils.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swm.client.render.entity.HologramRenderer;
import mod.syconn.swm.utils.general.ColorUtil;
import mod.syconn.swm.utils.general.ResourceUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

@Environment(EnvType.CLIENT)
public class HologramData { // TODO TO UPDATE THE TEXTURES FOR THE BAR IN THE HOLOGRAM look at MAPRENDERER, ALL ENTITY SUPPORT?

    private final HologramRenderer renderer;
    private final AbstractClientPlayer player;
    private final ResourceLocation skin;

    public HologramData(ClientLevel level, PlayerInfo playerInfo) {
        this.renderer = new HologramRenderer(this, playerInfo.getModelName().equals("slim"));
        this.player = new AbstractClientPlayer(level, playerInfo.getProfile()) {};
//        this.player = (AbstractClientPlayer) level.getPlayerByUUID(playerInfo.getProfile().getId()); TODO USE MORE LATER

        var texture = new DynamicTexture(ResourceUtil.loadResource(playerInfo.getSkinLocation()));
        ResourceUtil.modifyTexture(texture, (x, y, color) -> FastColor.ABGR32.color(140, ColorUtil.hologramColor(color)));
        this.skin = ResourceUtil.registerOrGet(playerInfo.getProfile().getName(), texture);
    }

    public HologramRenderer getRenderer() {
        return renderer;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    public ResourceLocation getSkin() {
        return skin;
    }
}
