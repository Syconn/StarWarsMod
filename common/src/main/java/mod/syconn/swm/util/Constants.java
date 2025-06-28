package mod.syconn.swm.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class Constants {

    public static final String MOD = "swm";
    public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
    public static final SimplexNoise SIMPLEX = new SimplexNoise(RANDOM);

    public static ResourceLocation withId(String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD, s);
    }
}
