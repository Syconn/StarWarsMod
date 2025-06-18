package mod.syconn.swm.util.block;

import net.minecraft.util.StringRepresentable;

public enum TwoPartBlock implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    private TwoPartBlock(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
