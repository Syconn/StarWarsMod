package mod.syconn.swm.fabric.client.data;

import com.mojang.math.Axis;
import mod.syconn.swm.features.blaster.server.data.BlasterJson;
import mod.syconn.swm.features.blaster.server.data.MuzzleData;
import mod.syconn.swm.utils.Constants;
import mod.syconn.swm.utils.client.NodeVec3;

import static mod.syconn.swm.features.addons.BlasterContent.BOLT;
import static mod.syconn.swm.features.addons.BlasterContent.RED;

public class BlasterDefaults {

    public enum BlasterTypes {

        F11("f11", new BlasterJson(Constants.withId("f11"), 2, empire(50, 120, 20, 5f, new NodeVec3(0.015625, -0.01875, -0.9625f)))),
        E11("e11", new BlasterJson(Constants.withId("e11"), 2, empire(65, 100, 15, 3.5f, new NodeVec3(-1.26562f, 0.275f, -0.04688f, Axis.YP.rotationDegrees(-90f)))));

        private final String id;
        private final BlasterJson data;

        BlasterTypes(String id, BlasterJson data) {
            this.id = id;
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public BlasterJson getData() {
            return data;
        }
    }

    private static MuzzleData empire(int tolerance, float range, float rate, float damage, NodeVec3 node) {
        return new MuzzleData(BOLT, RED, 0, tolerance, 20, range, rate, damage, 0.87f, false, MuzzleData.Holster.SIDEARM, node, 0.25f, 1, 1);
    }
}
