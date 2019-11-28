package com.github.iunius118.type18ballisticcomputer.client.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClientUtils {
    @Nullable
    public static RayTraceResult getMouseOver(double distance, float partialTicks) {
        if (distance < 0.0D) {
            return null;
        }

        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        Entity viewEntity = mc.getRenderViewEntity();
        Entity pointedEntity;
        RayTraceResult objectMouseOver = null;

        if (viewEntity != null && world != null) {
            pointedEntity = null;
            Vec3d vec3EntityPos = null;
            Vec3d vec3Eyes = viewEntity.getPositionEyes(partialTicks);
            Vec3d vec3Look = viewEntity.getLook(partialTicks);
            Vec3d vec3Reach = vec3Eyes.add(vec3Look.scale(distance));

            double d0 = distance;
            Vec3d vec3EyesBlock = viewEntity.getPositionEyes(partialTicks);
            Vec3d vec3ReachBlock = vec3EyesBlock.add(vec3Look.scale((d0 > 100.0D) ? 100.0D : d0));

            for (int c = (int) Math.ceil(d0 / 100.0D); c > 0; c--) {
                objectMouseOver = world.rayTraceBlocks(vec3EyesBlock, vec3ReachBlock, true, false, true);
                vec3EyesBlock = vec3ReachBlock;

                if (objectMouseOver != null) {
                    if (objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
                        break;
                    } else if (objectMouseOver.hitVec != null) {
                        vec3EyesBlock = objectMouseOver.hitVec;
                    }
                }

                d0 -= 100.0D;
                vec3ReachBlock = vec3ReachBlock.add(vec3Look.scale(Math.min(d0, 100.0D)));
            }

            double d1 = distance;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3Eyes);
            }

            float f = 1.0F;
            Predicate<Entity> predicate = entity -> (entity != null && entity.canBeCollidedWith());
            List<Entity> list = world.getEntitiesInAABBexcluding(viewEntity, viewEntity.getEntityBoundingBox().expand(vec3Look.x * d1, vec3Look.y * d1, vec3Look.z * d1).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, predicate));
            double d2 = d1;

            for (Entity entity1 : list) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3Eyes, vec3Reach);

                if (axisalignedbb.contains(vec3Eyes)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec3EntityPos = raytraceresult == null ? vec3Eyes : raytraceresult.hitVec;
                        d2 = 0.0D;
                    }

                } else if (raytraceresult != null) {
                    double d3 = vec3Eyes.distanceTo(raytraceresult.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        if (entity1.getLowestRidingEntity() == viewEntity.getLowestRidingEntity() && !viewEntity.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity1;
                                vec3EntityPos = raytraceresult.hitVec;
                            }

                        } else {
                            pointedEntity = entity1;
                            vec3EntityPos = raytraceresult.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new RayTraceResult(pointedEntity, vec3EntityPos);
            }
        }

        return objectMouseOver;
    }
}
