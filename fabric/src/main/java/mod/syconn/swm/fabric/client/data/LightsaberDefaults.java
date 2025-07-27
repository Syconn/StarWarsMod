package mod.syconn.swm.fabric.client.data;

import com.mojang.math.Axis;
import mod.syconn.swm.features.addons.LightsaberContent;
import mod.syconn.swm.features.lightsaber.data.BladeData;
import mod.syconn.swm.features.lightsaber.data.LightsaberJson;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.NodeVec3;

import java.util.ArrayList;
import java.util.List;

import static mod.syconn.swm.features.addons.LightsaberContent.*;

public class LightsaberDefaults {

    public enum LightsaberTypes {
        ANAKIN("anakin", createJSON("anakin", 2, true, 1.0f, 1, BLUE, PLASMA, List.of(new NodeVec3(0.00391f, 0.07031f, -0.00047f)))),
        LUKE("luke", createJSON("luke", 2, true, 1.0f, 1, GREEN, PLASMA, List.of(new NodeVec3(0.00071f, 0.02188f, 0f)))),
        MACE("mace", createJSON("mace", 2, true, 1.0f, 0.95f, PURPLE, PLASMA, List.of(new NodeVec3(0.00391f, -0.05469f, -0.00047f)))),
        OBI_WAN("obi", createJSON("obi", 2, true, 1.0f, 1, BLUE, PLASMA, List.of(new NodeVec3(0.00391f, 0.18906f, -0.00047)))),
        YODA("yoda", createJSON("yoda", 2, true, 1.2f, 0.85, GREEN, PLASMA, List.of(new NodeVec3(0f, -0.12656f, 0f)))),
        AHSOKA("ahsoka", createJSON("ahsoka", 2, true, 1.0f, 0.85f, GREEN, PLASMA, List.of(new NodeVec3(0.00391f, -0.06719f, -0.00047f)))),
        DARK_SABER("dark_saber", createJSON("dark_saber", 2, true, 1f, 1, WHITE, LightsaberContent.DARK_SABER, List.of(new NodeVec3(0f, 0.62500f, 0f, Axis.YN.rotationDegrees(180f))))),
        TEMPLE_GUARD("temple_guard", createJSON("temple_guard", 2, true, 1.0f, 0.95f, YELLOW, PLASMA, List.of(new NodeVec3(0.00312f, 0.01250f, -0.00156f)))),
        KAL("kal", createJSON("kal", 3, true, 1.6f, 0.95f, BLUE, PLASMA, List.of(new NodeVec3(0.00078f, 0.30469f, -0.00047f)))),
        KYLO("kylo", createJSON("kylo", 2, false, 1.0f, 0.15f, 1, RED, PLASMA, List.of(new NodeVec3(0.00234f, 0.00781f, -0.00203f)),
                List.of(new NodeVec3(-0.12734f, -0.08594f, -0.00203f, Axis.ZP.rotationDegrees(90f)), new NodeVec3(0.12734f, -0.08594f, -0.00203f, Axis.ZP.rotationDegrees(-90f))))),
        MAUL("maul", createJSON("maul", 2, true, 0.65f, 0.65f, RED, PLASMA, List.of(new NodeVec3(-0.00078f, 0.10625f, -0.00359f),
                new NodeVec3(-0.00078f, -0.49844f, -0.00359f, Axis.ZP.rotationDegrees(180f)))));

        private final String id;
        private final LightsaberJson data;

        LightsaberTypes(String id, LightsaberJson data) {
            this.id = id;
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public LightsaberJson getData() {
            return data;
        }
    }

    private static LightsaberJson createJSON(String model, int version, boolean stable, float bladeLengthScalar, double radius, int color, String bladeType, List<NodeVec3> nodes) {
        return new LightsaberJson(Constants.withId(model), version, simpleBlade(stable, bladeLengthScalar, radius, color, bladeType, nodes));
    }

    private static LightsaberJson createJSON(String model, int version, boolean stable, float primaryScalar, float secondaryScalar, double radius, int color, String bladeType, List<NodeVec3> primary, List<NodeVec3> secondary) {
        return new LightsaberJson(Constants.withId(model), version, primarySecondaryBlade(stable, primaryScalar, secondaryScalar, radius, color, bladeType, primary, secondary));
    }

    private static List<BladeData> simpleBlade(boolean stable, float bladeLengthScalar, double radius, int color, String bladeType, List<NodeVec3> nodes) {
        var data = new ArrayList<BladeData>();
        nodes.forEach(node -> data.add(new BladeData(stable, false, bladeLengthScalar, (byte) 0, radius, color, bladeType, node)));
        return data;
    }

    private static List<BladeData> primarySecondaryBlade(boolean stable, float primaryScalar, float secondaryScalar, double radius, int color, String bladeType, List<NodeVec3> primary, List<NodeVec3> secondary) {
        var data = new ArrayList<BladeData>();
        primary.forEach(node -> data.add(new BladeData(stable, false, primaryScalar, (byte) 0, radius, color, bladeType, node)));
        secondary.forEach(node -> data.add(new BladeData(stable, false, secondaryScalar, (byte) 0, radius, color, bladeType, node)));
        return data;
    }
}
