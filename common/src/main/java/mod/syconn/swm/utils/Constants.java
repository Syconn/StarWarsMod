package mod.syconn.swm.utils;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD = "swm";
    public static final Logger LOG = LoggerFactory.getLogger(MOD);
    public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
    public static final SimplexNoise SIMPLEX = new SimplexNoise(RANDOM);
    public static final UpdateTracker TRACKER = new UpdateTracker();

    public static ResourceLocation withId(String s) {
        return new ResourceLocation(MOD, s);
    }

    public static ModelResourceLocation withId(String loc, String path) {
        return new ModelResourceLocation(MOD, loc, path);
    }
}
