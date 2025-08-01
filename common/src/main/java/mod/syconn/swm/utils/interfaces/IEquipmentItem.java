package mod.syconn.swm.utils.interfaces;

public interface IEquipmentItem {

    SWEquipmentSlot getSWEquipmentSlot();

    enum SWEquipmentSlot {
        LIGHTSABER(0),
        BLASTER_BACK(1),
        BLASTER_SIDE(2);

        private final int slot;

        SWEquipmentSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public static SWEquipmentSlot getSlot(int slot) {
            for (var equipment : values()) if (equipment.slot == slot) return equipment;
            return LIGHTSABER;
        }
    }
}
