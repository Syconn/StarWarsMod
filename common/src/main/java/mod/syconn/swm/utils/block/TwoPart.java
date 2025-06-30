package mod.syconn.swm.utils.block;

import net.minecraft.util.StringRepresentable;

public enum TwoPart implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    TwoPart(String name) {
        this.name = name;
    }

    public boolean right() {
        return this == RIGHT;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
