package mod.syconn.swm.utils.client;

import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.render.entity.HologramRenderer;
import mod.syconn.swm.utils.general.ColorUtil;
import mod.syconn.swm.utils.general.ResourceUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class HologramData { // TODO TO UPDATE THE TEXTURES FOR THE BAR IN THE HOLOGRAM look at MAPRENDERER, ALL ENTITY SUPPORT?

    private final Minecraft minecraft = GameInstance.getClient();
    private final HologramRenderer renderer;
    private final AbstractClientPlayer player;
    private final ResourceLocation skin;
    private final int textureHeight;

    public HologramData(CompoundTag tag) {
        this(tag.getUUID("uuid"));
    }

    public HologramData(UUID uuid) {
        var playerInfo = this.minecraft.player.connection.getPlayerInfo(uuid);

        this.renderer = new HologramRenderer(this, playerInfo.getModelName().equals("slim"));
        this.player = new AbstractClientPlayer(this.minecraft.level, playerInfo.getProfile()) {};
//        this.player = (AbstractClientPlayer) level.getPlayerByUUID(playerInfo.getProfile().getId()); TODO USE MORE LATER

        var texture = new DynamicTexture(ResourceUtil.loadResource(playerInfo.getSkinLocation()));
        ResourceUtil.modifyTexture(texture, (x, y, color) -> FastColor.ABGR32.color(140, ColorUtil.hologramColor(color)));
        this.skin = ResourceUtil.registerOrGet(playerInfo.getProfile().getName(), texture);
        this.textureHeight = texture.getPixels().getHeight();
    }

    public void tick() {

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

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putUUID("uuid", this.player.getUUID());
        return tag;
    }
}
