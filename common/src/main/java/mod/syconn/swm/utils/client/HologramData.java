package mod.syconn.swm.utils.client;

import com.mojang.authlib.GameProfile;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.render.entity.HologramRenderer;
import mod.syconn.swm.utils.general.ColorUtil;
import mod.syconn.swm.utils.general.ResourceUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class HologramData { // TODO TO UPDATE THE TEXTURES FOR THE BAR IN THE HOLOGRAM look at MAPRENDERER, ALL ENTITY SUPPORT?

    private final HologramRenderer renderer;
    private final AbstractClientPlayer player;
    private final ResourceLocation skin;
    private final int textureHeight;

    public HologramData(@NotNull UUID uuid) {
        final var minecraft = GameInstance.getClient();
        final var playerInfo = getPlayerInfo(minecraft, uuid);
        this.renderer = new HologramRenderer(this, playerInfo.getModelName().equals("slim"));
        this.player = new AbstractClientPlayer(minecraft.level, playerInfo.getProfile()) {};
//        this.player = (AbstractClientPlayer) level.getPlayerByUUID(playerInfo.getProfile().getId()); TODO USE MORE LATER

        var texture = new DynamicTexture(ResourceUtil.loadResource(playerInfo.getSkinLocation()));
        ResourceUtil.modifyTexture(texture, (x, y, color) -> FastColor.ABGR32.color(160, ColorUtil.hologramColor(color)));
        this.skin = ResourceUtil.registerOrGet(playerInfo.getProfile().getName(), texture);
        this.textureHeight = texture.getPixels().getHeight();
    }

    private PlayerInfo getPlayerInfo(Minecraft minecraft, UUID uuid) {
        final var playerInfo = minecraft.player.connection.getPlayerInfo(uuid);
        if (playerInfo == null) return new PlayerInfo(new GameProfile(uuid, "Offline-Player"), false);
        return playerInfo;
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

    public static class HologramTag {

        private static final String ID = "hologramData";

        public UUID uuid;
        public boolean refresh;

        public HologramTag(@Nullable UUID uuid, Boolean refresh) {
            this.uuid = uuid;
            this.refresh = refresh;
        }

        public HologramTag(CompoundTag tag) {
            this.uuid = tag.hasUUID("uuid") ? tag.getUUID("uuid") : null;
            this.refresh = tag.getBoolean("refresh");
        }

        private CompoundTag save() {
            var tag = new CompoundTag();
            if (this.uuid != null) tag.putUUID("uuid", this.uuid);
            tag.putBoolean("refresh", this.refresh);
            return tag;
        }

        public static HologramTag getOrCreate(ItemStack stack) {
            if (!stack.getOrCreateTag().contains(ID)) return create();
            return new HologramTag(stack.getOrCreateTag().getCompound(ID));
        }

        public static void update(ItemStack stack, UUID uuid) {
            var holo = getOrCreate(stack);
            holo.uuid = !uuid.equals(holo.uuid) ? uuid : null;
            holo.refresh = true;
            holo.change(stack);
        }

        public static boolean refreshed(ItemStack stack) {
            var holo = getOrCreate(stack);
            var refresh = holo.refresh;
            holo.refresh = false;
            holo.change(stack);
            return refresh;
        }

        private static HologramTag create() {
            return new HologramTag(null, false);
        }

        public void change(ItemStack stack) {
            stack.getOrCreateTag().put(ID, save());
        }
    }
}
