package mod.syconn.swm.fabric.client.data;

import mod.syconn.swm.features.blaster.server.data.BlasterJson;
import mod.syconn.swm.features.blaster.server.data.MuzzleData;
import mod.syconn.swm.utils.Constants;

import static mod.syconn.swm.features.addons.BlasterContent.BOLT;
import static mod.syconn.swm.features.addons.BlasterContent.RED;

public class BlasterDefaults {

    public enum BlasterTypes {

        F11("f11", new BlasterJson(Constants.withId("f11"), 1, empire(50, 120, 20, 5))),
        E11("e11", new BlasterJson(Constants.withId("e11"), 1, empire(65, 100, 15, 3.5f)));

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

    private static MuzzleData empire(int tolerance, float range, float rate, float damage) {
        return new MuzzleData(BOLT, RED, 0, tolerance, 20, range, rate, damage, 0.92f, false, MuzzleData.Holster.SIDEARM);
    }
}
