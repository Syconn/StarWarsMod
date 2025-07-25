package mod.syconn.swm.fabric.client.data;

import com.mojang.math.Axis;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.data.LightsaberData;
import mod.syconn.swm.utils.client.NodeVec3;

import java.util.List;

import static mod.syconn.swm.features.addons.LightsaberContent.*;

public class LightsaberDefaults {

    public enum LightsaberTypes {
        ANAKIN("anakin", new LightsaberData(0, true, 1.2f, 1, BLUE, PLASMA, List.of(new NodeVec3(0, -0.085, 0)))),
        LUKE("luke", new LightsaberData(1, true, 1.2f, 1, GREEN, PLASMA, List.of(new NodeVec3(0.00071f, 0.02188f, 0f)))),
        MACE("mace", new LightsaberData(2, true, 1.2f, 0.95f, PURPLE, PLASMA, List.of(new NodeVec3(0.00391f, -0.05312f, -0.00047f)))),
        OBI_WAN("obi", new LightsaberData(3, true, 1.2f, 1, BLUE, PLASMA, List.of(new NodeVec3(0, -0.205, 0)))),
        YODA("yoda", new LightsaberData(4, true, 1.2f, 0.85, GREEN, PLASMA, List.of(new NodeVec3(0f, -0.12656f, 0f)))),
        AHSOKA("ahsoka", new LightsaberData(5, true, 1.2f, 1, GREEN, PLASMA, List.of(new NodeVec3(0, 0.05, 0)))),
        DARK_SABER("dark_saber", new LightsaberData(6, true, 1.2f, 1, WHITE, LightsaberContent.DARK_SABER, List.of(new NodeVec3(0, -0.6, 0, Axis.YN.rotationDegrees(180f))))),
        TEMPLE_GUARD("temple_guard", new LightsaberData(7, true, 1.2f, 0.95f, YELLOW, PLASMA, List.of(new NodeVec3(0.00312f, 0.01250f, -0.00156f)))),
        KAL("kal", new LightsaberData(8, true, 1.2f, 1, BLUE, PLASMA, List.of(new NodeVec3(0, -0.325, 0)))),
        KYLO("kylo", new LightsaberData(9, false, 1.2f, 1, RED, PLASMA, List.of(new NodeVec3(-0.0025, -0.005, 0),
                new NodeVec3(0.125, 0.075, 0, Axis.ZP.rotationDegrees(90f), 0.15f/1.1f), new NodeVec3(-0.125, 0.075, 0, Axis.ZP.rotationDegrees(-90f), 0.15f/1.1f)))),
        MAUL("maul", new LightsaberData(10, true, 1.2f, 0.65f, RED, PLASMA, List.of(new NodeVec3(0, -0.1, 0),
                new NodeVec3(0, 0.45, 0, Axis.ZP.rotationDegrees(180f)))));

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
