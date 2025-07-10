package mod.syconn.swm.utils.client;

import com.mojang.authlib.GameProfile;
import dev.architectury.utils.GameInstance;
import mod.syconn.swm.client.render.entity.HologramRenderer;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.generic.AnimationUtil;
import mod.syconn.swm.utils.generic.ColorUtil;
import mod.syconn.swm.utils.generic.MathUtil;
import mod.syconn.swm.utils.generic.ResourceUtil;
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
public class HologramData {

    public static final byte TRANSITION_TICKS = 16;
    private final HologramRenderer renderer;
    private final AbstractClientPlayer player;
    private final ResourceLocation skin;
    private final boolean item;
    private final int textureHeight = 64;
    private Runnable endCall = null;
    private int transition;
    private int scanBarTicks = 0;
    private int scanBar1 = 0;
    private int scanBar2 = 16;

    public HologramData(@NotNull UUID uuid, boolean item) {
        final var minecraft = GameInstance.getClient();
        final var playerInfo = getPlayerInfo(minecraft, uuid);
        final var clientPlayer = item ? null : minecraft.level.getPlayerByUUID(playerInfo.getProfile().getId());
        final var texture = new DynamicTexture(ResourceUtil.loadResource(playerInfo.getSkinLocation()));
        ResourceUtil.modifyTexture(texture, this::getPixelColor);

        this.item = item;
        this.renderer = new HologramRenderer(this, playerInfo.getModelName().equals("slim"));
        this.player = clientPlayer != null ? (AbstractClientPlayer) clientPlayer : new AbstractClientPlayer(minecraft.level, playerInfo.getProfile()) {};
        this.skin = ResourceUtil.registerOrGet(playerInfo.getProfile().getName(), texture);
        this.transition = TRANSITION_TICKS;
    }

    private PlayerInfo getPlayerInfo(Minecraft minecraft, UUID uuid) {
        final var playerInfo = minecraft.player.connection.getPlayerInfo(uuid);
        if (playerInfo == null) return new PlayerInfo(new GameProfile(uuid, "Offline-Player"), false);
        return playerInfo;
    }

    public void tick() {
        if (this.transition > 0) this.transition--;
        if (this.transition < 0) this.transition++;

        this.scanBarTicks++;
        if (this.scanBarTicks >= 2) {
            this.scanBarTicks = 0;

            var texture = new DynamicTexture(ResourceUtil.loadResource(player.getSkinTextureLocation()));
            if (this.scanBar1 >= this.textureHeight) this.scanBar1 = 0;
            if (this.scanBar2 >= this.textureHeight) this.scanBar2 = 0;

            ResourceUtil.modifyTexture(texture, this::getPixelColor);

            ResourceUtil.registerOrGet(player.getName().getString(), texture);

            this.scanBar1++;
            this.scanBar2++;
        }
    }

    private int getPixelColor(int x, int y, int rgba) {
        if (FastColor.ARGB32.alpha(rgba) == 0) return rgba;
        return FastColor.ABGR32.color(scanBar(y) ? 255 : 160, scanBar(y) ? ColorUtil.packArgb(192, 192, 192, 100) : ColorUtil.hologramColor(rgba));
    }

    public float getAnimationScale(float partialTicks) {
        if (this.transition == 0) {
            var ret = this.endCall != null ? 0 : 1;
            if (this.endCall != null) {
                this.endCall.run();
                this.endCall = null;
            }
            return ret;
        }
        if (this.transition > 0) return AnimationUtil.outCubic(1 - (this.transition - partialTicks) / TRANSITION_TICKS);
        return AnimationUtil.inCubic(-(this.transition + partialTicks) / TRANSITION_TICKS);
    }

    public void endCall(Runnable endCall) {
        this.endCall = endCall;
        this.transition = -TRANSITION_TICKS;
    }

    public int getTransition() {
        return transition;
    }

    private boolean scanBar(int y) { // TODO FIX PARTIAL TICKS?
        return this.scanBar1 == y || MathUtil.wrap(this.scanBar1 - 32, this.textureHeight) == y || this.scanBar2 == y || MathUtil.wrap(this.scanBar2 - 32, this.textureHeight) == y;
    }

    public boolean shouldRender() {
        return Constants.RANDOM.nextInt(55) != 0;
    }

    public boolean isItem() {
        return item;
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

        public final UUID itemId;
        public UUID uuid;

        public HologramTag(@Nullable UUID uuid) {
            this.uuid = uuid;
            this.itemId = UUID.randomUUID();
        }

        public HologramTag(CompoundTag tag) {
            this.uuid = tag.hasUUID("uuid") ? tag.getUUID("uuid") : null;
            this.itemId = tag.hasUUID("id") ? tag.getUUID("id") : UUID.randomUUID();
        }

        private CompoundTag save() {
            var tag = new CompoundTag();
            if (this.uuid != null) tag.putUUID("uuid", this.uuid);
            tag.putUUID("id", this.itemId);
            return tag;
        }

        public static HologramTag getOrCreate(ItemStack stack) {
            var tag = !stack.getOrCreateTag().contains(ID) ? create() : new HologramTag(stack.getOrCreateTag().getCompound(ID));
            tag.change(stack);
            return tag;
        }

        public static void update(ItemStack stack, UUID uuid) {
            var holo = getOrCreate(stack);
            holo.uuid = uuid;
            holo.change(stack);
        }

        private static HologramTag create() {
            return new HologramTag((UUID) null);
        }

        public void change(ItemStack stack) {
            stack.getOrCreateTag().put(ID, save());
        }
    }
}
