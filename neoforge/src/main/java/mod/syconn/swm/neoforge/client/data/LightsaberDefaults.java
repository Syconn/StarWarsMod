package mod.syconn.swm.neoforge.client.data;

import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LightsaberDefaults {

    public static final int GREEN = ColorUtil.packHsv(0.36f, 1f, 0.5f);
    public static final int BLUE = ColorUtil.packHsv(0.6f, 0.85f, 0.5f);
    public static final int PURPLE = ColorUtil.packHsv(0.8f, 1f, 0.5f);

    public enum LightsaberTypes {
        ANAKIN("anakin", new LightsaberData(0, true, 1.2, BLUE, List.of(new Vec3(-0.025, -0.15, 0.03)))),
        LUKE("luke", new LightsaberData(1, true, 1.6, GREEN, List.of(new Vec3(0.02, -0.025, 0.135)))),
        MACE("mace", new LightsaberData(2, true, 1.4, PURPLE, List.of(new Vec3(0.045, 0, -0.04)))),
        OBI_WAN("obi", new LightsaberData(3, true, 1.4, BLUE, List.of(new Vec3(-0, -0.21, 0)))),
        YODA("yoda", new LightsaberData(4, true, 1.4, GREEN, List.of(new Vec3(0, 0.8, 0))));

        private final String id;
        private final LightsaberData data;

        LightsaberTypes(String id, LightsaberData data) {
            this.id = id;
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public LightsaberData getData() {
            return data;
        }
    }
}
