package mod.syconn.swm.forge.client.data;

import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.util.client.model.NodeVec3;
import mod.syconn.swm.util.math.ColorUtil;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.List;

public class LightsaberDefaults {

    public static final int GREEN = ColorUtil.packHsv(0.36f, 1f, 0.5f);
    public static final int BLUE = ColorUtil.packHsv(0.6f, 0.85f, 0.5f);
    public static final int PURPLE = ColorUtil.packHsv(0.8f, 1f, 0.5f);
    public static final int YELLOW = ColorUtil.packHsv(0.17f, 0.85f, 0.5f);
    public static final int RED = ColorUtil.packHsv(0f, 0.85f, 0.5f);

    public enum LightsaberTypes {
        ANAKIN("anakin", new LightsaberData(0, true, 1.2f, 1, BLUE, List.of(new NodeVec3(0, -0.085, 0, new Quaternionf())))),
        LUKE("luke", new LightsaberData(1, true, 1.1f, 1, GREEN, List.of(new NodeVec3(0, -0.05, 0.01, new Quaternionf())))),
        MACE("mace", new LightsaberData(2, true, 1.4f, 1, PURPLE, List.of(new NodeVec3(0.045, 0, -0.04, new Quaternionf())))),
        OBI_WAN("obi", new LightsaberData(3, true, 1.4f, 1, BLUE, List.of(new NodeVec3(0, -0.205, 0, new Quaternionf())))),
        YODA("yoda", new LightsaberData(4, true, 1.4f, 0.85, GREEN, List.of(new NodeVec3(0, 0.125, 0, new Quaternionf())))),
        AHSOKA("ahsoka", new LightsaberData(5, true, 1.2f, 1, GREEN, List.of(new NodeVec3(0, 0.05, 0, new Quaternionf())))),
        DARK_SABER("dark_saber", new LightsaberData(6, true, 1.2f, 1, -1, List.of(new NodeVec3(-0.025, -0.15, 0.03, new Quaternionf())))),
        TEMPLE_GUARD("temple_guard", new LightsaberData(7, true, 1.1f, 1, YELLOW, List.of(new NodeVec3(0, 0, 0, new Quaternionf())))),
        KAL("kal", new LightsaberData(8, true, 1.6f, 1, BLUE, List.of(new NodeVec3(0, -0.325, 0, new Quaternionf())))),
        KYLO("kylo", new LightsaberData(9, false, 1.1f, 1, RED, List.of(new NodeVec3(0, -0.15, 0, new Quaternionf())))),
        MAUL("maul", new LightsaberData(10, true, 1.2f, 1, RED, List.of(new NodeVec3(-0.025, -0.15, 0.03, new Quaternionf()))));

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
