package mod.syconn.swm.features.lightsaber.network;

import mod.syconn.swm.api.network.message.Packet;
import mod.syconn.swm.api.network.message.PacketContext;
import mod.syconn.swm.features.lightsaber.blockentity.LightsaberWorkbenchBlockEntity;
import mod.syconn.swm.registry.ModRecipes;
import mod.syconn.swm.utils.server.StackedIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CraftHiltPacket extends Packet<CraftHiltPacket> {

    private final BlockPos pos;
    private final ResourceLocation id;

    public CraftHiltPacket(BlockPos pos, ResourceLocation id) {
        this.pos = pos;
        this.id = id;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeResourceLocation(this.id);
    }

    @Override
    public void encode(CraftHiltPacket message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeResourceLocation(message.id);
    }

    @Override
    public CraftHiltPacket decode(FriendlyByteBuf buffer) {
        return new CraftHiltPacket(buffer.readBlockPos(), buffer.readResourceLocation());
    }

    @Override
    public void handle(CraftHiltPacket message, PacketContext context) {
        context.execute(() -> {
            var player = context.getPlayer();
            if (player.level().getBlockEntity(message.pos) instanceof LightsaberWorkbenchBlockEntity be && !be.hasItem()) {
                var recipe = ModRecipes.getRecipeFromId(ModRecipes.LIGHTSABER.get(), player.level(), message.id);

                if (recipe.isPresent()) {
                    for (StackedIngredient ingredient : recipe.get().ingredients()) {
                        int count = ingredient.count();
                        for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                            if (ingredient.ingredient().test(player.getInventory().getItem(j))) {
                                int num = player.getInventory().getItem(j).getCount();
                                if (num >= count) {
                                    player.getInventory().removeItem(j, count);
                                    count -= num;
                                } else {
                                    count -= num;
                                    player.getInventory().removeItem(j, num);
                                }
                            }
                        }
                    }
                    be.getContainer().setItem(0, recipe.get().item().copy());
                }
            }
        });
        context.setHandled(true);
    }
}
