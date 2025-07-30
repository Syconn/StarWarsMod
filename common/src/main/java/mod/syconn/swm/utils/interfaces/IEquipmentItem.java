package mod.syconn.swm.utils.interfaces;

public interface IEquipmentItem {

    SWEquipmentSlot getSWEquipmentSlot();

    enum SWEquipmentSlot {
        LIGHTSABER,
        BLASTER_BACK,
        BLASTER_SIDE
    }
}
