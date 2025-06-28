package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.util.Constants;
import mod.syconn.swm.util.server.StackedIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public record CraftHiltPacket(BlockPos pos, ResourceLocation id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CraftHiltPacket> TYPE = new CustomPacketPayload.Type<>(Constants.withId("craft_hilt"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftHiltPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, CraftHiltPacket::pos,
            ResourceLocation.STREAM_CODEC, CraftHiltPacket::id, CraftHiltPacket::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CraftHiltPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer().level().getBlockEntity(packet.pos) instanceof LightsaberWorkbenchBlockEntity blockEntity &&
                    context.getPlayer() instanceof ServerPlayer sp && !blockEntity.hasItem()) {
                var recipe = ModRecipes.getRecipeFromId(ModRecipes.LIGHTSABER.get(), sp.level(), packet.id);

                if (recipe.isPresent()) {
                    for (StackedIngredient ingredient : recipe.get().value().materials()) {
                        int count = ingredient.count();
                        for (int j = 0; j < sp.getInventory().getContainerSize(); j++) {
                            if (ingredient.ingredient().test(sp.getInventory().getItem(j))){
                                int num = sp.getInventory().getItem(j).getCount();
                                if (num >= count){
                                    sp.getInventory().removeItem(j, count);
                                    count -= num;
                                } else {
                                    count -= num;
                                    sp.getInventory().removeItem(j, num);
                                }
                            }
                        }
                    }
                    blockEntity.getContainer().setItem(0, recipe.get().value().result().copy());
                }
            }
        });
    }
}
