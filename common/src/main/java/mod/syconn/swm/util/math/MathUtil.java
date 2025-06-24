package mod.syconn.swm.util.math;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class MathUtil {

    public static final Vec3 V3D_POS_X = new Vec3(1, 0, 0);
    public static final Vec3 V3D_NEG_X = new Vec3(-1, 0, 0);
    public static final Vec3 V3D_POS_Y = new Vec3(0, 1, 0);
    public static final Vec3 V3D_NEG_Y = new Vec3(0, -1, 0);
    public static final Vec3 V3D_POS_Z = new Vec3(0, 0, 1);
    public static final Vec3 V3D_NEG_Z = new Vec3(0, 0, -1);

    public static final Vector3f V3F_POS_X = new Vector3f(1, 0, 0);
    public static final Vector3f V3F_NEG_X = new Vector3f(-1, 0, 0);
    public static final Vector3f V3F_POS_Y = new Vector3f(0, 1, 0);
    public static final Vector3f V3F_NEG_Y = new Vector3f(0, -1, 0);
    public static final Vector3f V3F_POS_Z = new Vector3f(0, 0, 1);
    public static final Vector3f V3F_NEG_Z = new Vector3f(0, 0, -1);

    public static float remap(float x, float iMin, float iMax, float oMin, float oMax) {
        return (x - iMin) / (iMax - iMin) * (oMax - oMin) + oMin;
    }

    public static Vec3 anglesToLook(float pitch, float yaw) {
        var x = -Mth.sin(yaw * Mth.RAD_TO_DEG) * Mth.cos(pitch * Mth.RAD_TO_DEG);
        var y = -Mth.sin(pitch * Mth.RAD_TO_DEG);
        var z = Mth.cos(yaw * Mth.RAD_TO_DEG) * Mth.cos(pitch * Mth.RAD_TO_DEG);

        return new Vec3(x, y, z).normalize();
    }

    public static Vec3 lookToAngles(Vec3 forward) {
        forward = forward.normalize();

        var yaw = -(float)Math.atan2(forward.x, forward.z);
        var pitch = -(float)Math.asin(forward.y);

        return new Vec3(pitch * Mth.DEG_TO_RAD, yaw * Mth.DEG_TO_RAD, 0);
    }

    public static float toRadians(float degrees) {
        return degrees * Mth.RAD_TO_DEG;
    }

    public static Vec3 transform(Vec3 v, Matrix4f transform) {
        var vec3d = new Vector3d(v.x, v.y, v.z).mulPosition(transform);
        return new Vec3(vec3d.x, vec3d.y, vec3d.z);
    }

    public static void scalePos(PoseStack stack, float x, float y, float z) {
        var entry = stack.last();
        entry.pose().scale(x, y, z);
    }

    public static Quaternionf getRotation(Direction direction) {
        return switch (direction) {
            case DOWN -> new Quaternionf().rotationXYZ(0, 0, (float)(Math.PI / -2));
            case UP -> new Quaternionf().rotationXYZ(0, 0, (float)(Math.PI / 2));
            case NORTH -> new Quaternionf().rotationXYZ(0, (float)(Math.PI / 2), 0);
            case SOUTH -> new Quaternionf().rotationXYZ(0, (float)(Math.PI / -2), 0);
            case WEST -> new Quaternionf().rotationXYZ(0, (float)Math.PI, 0);
            case EAST -> new Quaternionf().rotationXYZ(0, 0, 0);
        };
    }

    public static Vec3i reflect(Vec3i incident, Vec3i normal) {
        var reflection = normal.multiply(2 * normal.distManhattan(incident)).subtract(incident);
        return reflection.multiply(-1);
    }

    public static int wrap(int value, int max) {
        if (value < 0) return max;
        if (value > max) return 0;
        return value;
    }
}
