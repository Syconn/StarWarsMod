package mod.syconn.swm.integration.jei.subtypes;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mod.syconn.swm.features.lightsaber.data.LightsaberTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LightsaberSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

    public static final LightsaberSubtypeInterpreter INSTANCE = new LightsaberSubtypeInterpreter();

    @Override
    public @NotNull String apply(@NotNull ItemStack itemStack, @NotNull UidContext context) {
        var lT = LightsaberTag.getOrCreate(itemStack);
        System.out.println(itemStack);
        return String.valueOf(lT.model);
    }
}
