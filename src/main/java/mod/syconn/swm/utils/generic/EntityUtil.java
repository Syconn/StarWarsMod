package mod.syconn.swm.utils.generic;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityUtil {

    public static EntityHitResult raycastEntities(Class<? extends Entity> clazz, Vec3 startPos, Vec3 fromDir, double distance, Entity fromEntity, Entity[] exclude) {
        Entity pointedEntity = null;
        Vec3 hitLocation = null;

        var blacklist = Arrays.asList(exclude);

        fromDir = fromDir.normalize();

        var endPos = startPos.add(fromDir.scale(distance));
        var list = fromEntity.level().getEntitiesOfClass(clazz, fromEntity.getBoundingBox().inflate(fromDir.x * distance, fromDir.y * distance, fromDir.z * distance).inflate(1, 1, 1), e -> !e.isSpectator());

        for (var entity : list) {
            if (blacklist.contains(entity)) continue;

            if (entity.canBeHitByProjectile()) {
                var box = entity.getBoundingBox();
                var hitvec = box.clip(startPos, endPos);

                if (hitvec.isPresent()) {
                    var distanceTo = startPos.distanceTo(hitvec.get());

                    if (distanceTo < distance) {
                        pointedEntity = entity;
                        distance = distanceTo;
                        hitLocation = hitvec.get();
                    }
                }
            }
        }

        if (pointedEntity != null) return new EntityHitResult(pointedEntity, hitLocation);
        return null;
    }

    public static ArrayList<Entity> raycastEntitiesCone(Vec3 startPos, Vec3 fromDir, double maxAngleRad, double distance, Entity fromEntity, Entity[] exclude) {
        var blacklist = Arrays.asList(exclude);

        fromDir = fromDir.normalize();

        var list = fromEntity.level().getEntitiesOfClass(LivingEntity.class, fromEntity.getBoundingBox().inflate(fromDir.x * distance, fromDir.y * distance, fromDir.z * distance).inflate(1, 1, 1), e -> !e.isSpectator());

        var hit = new ArrayList<Entity>();

        for (var entity : list) {
            if (blacklist.contains(entity)) continue;

            if (entity.canBeHitByProjectile()) {
                var entityDirVec = entity.position().subtract(startPos).normalize();
                if (Math.acos(entityDirVec.dot(fromDir)) > maxAngleRad) continue;
                hit.add(entity);
            }
        }

        return hit;
    }

    public static BlockHitResult raycastBlocks(Vec3 startPos, Vec3 fromDir, double distance, Entity fromEntity, ClipContext.Block shapeType, ClipContext.Fluid fluidHandling) {
        var end = startPos.add(fromDir.scale(distance));
        return fromEntity.level().clip(new ClipContext(startPos, end, shapeType, fluidHandling, fromEntity));
    }

    public static void setVelocityFromAngles(Entity entity, float pitch, float yaw, float scalar) {
        var look = MathUtil.anglesToLook(pitch, yaw);
        entity.setDeltaMovement(scalar * look.x, scalar * look.y, scalar * look.z);
    }

    public static void updateEulerRotation(Entity entity, Quaternionf rotation) {
        entity.xRotO = entity.getXRot();
        entity.yRotO = entity.getYRot();

        var eulerAngle = MathUtil.toEulerAngles(rotation);
        entity.setXRot((float) eulerAngle.x);
        entity.setYRot((float) eulerAngle.y);

        while (entity.getYRot() - entity.yRotO >= 180.0F) entity.yRotO += 360.0F;
        while (entity.getXRot() - entity.xRotO < -180.0F) entity.xRotO -= 360.0F;
        while (entity.getXRot() - entity.xRotO >= 180.0F) entity.xRotO += 360.0F;
    }
}
