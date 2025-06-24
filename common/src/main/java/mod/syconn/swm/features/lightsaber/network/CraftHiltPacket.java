package mod.syconn.swm.features.lightsaber.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swm.core.ModRecipes;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.util.server.StackedIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class CraftHiltPacket {

    private final BlockPos pos;
    private final ResourceLocation id;

    public CraftHiltPacket(BlockPos pos, ResourceLocation id) {
        this.pos = pos;
        this.id = id;
    }

    public CraftHiltPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeResourceLocation(this.id);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer().level().getBlockEntity(this.pos) instanceof LightsaberWorkbenchBlockEntity blockEntity && context.get().getPlayer() instanceof ServerPlayer sp && !blockEntity.hasItem()) {
                var recipe = ModRecipes.getRecipeFromId(ModRecipes.LIGHTSABER.get(), sp.level(), this.id);

                if (recipe.isPresent()) {
                    for (StackedIngredient ingredient : recipe.get().ingredients()) {
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
                    blockEntity.getContainer().setItem(0, recipe.get().item().copy());
                }
            }
        });
    }
}
