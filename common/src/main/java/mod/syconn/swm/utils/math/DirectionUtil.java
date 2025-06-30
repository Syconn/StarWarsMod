package mod.syconn.swm.utils.math;

import net.minecraft.core.Direction;

import java.util.Map;

public class DirectionUtil {

    public static <T> Map<Direction, T> dataList(T north, T south, T west, T east) {
        return Map.of(Direction.NORTH, north, Direction.SOUTH, south, Direction.WEST, west, Direction.EAST, east);
    }

    public static <T> Map<Direction, T> dataList(T north, T south, T west, T east, T up, T down) {
        return Map.of(Direction.NORTH, north, Direction.SOUTH, south, Direction.WEST, west, Direction.EAST, east, Direction.UP, up, Direction.DOWN, down);
    }
}
