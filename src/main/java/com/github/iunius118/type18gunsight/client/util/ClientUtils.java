package com.github.iunius118.type18gunsight.client.util;

import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Optional;

public class ClientUtils {
    @Nullable
    public static RayTraceResult getMouseOver(double distance, float partialTicks) {
        if (distance < 0.0D) {
            return null;
        }

        Minecraft mc = Minecraft.getInstance();
        World world = mc.world;
        Entity viewEntity = mc.getRenderViewEntity();
        Entity pointedEntity;
        RayTraceResult objectMouseOver = null;

        if (viewEntity != null && world != null) {
            pointedEntity = null;
            Vec3d vec3EntityPos = null;
            Vec3d vec3Eyes = viewEntity.getEyePosition(partialTicks);
            Vec3d vec3Look = viewEntity.getLook(partialTicks);
            Vec3d vec3Reach = vec3Eyes.add(vec3Look.scale(distance));

            double d0 = distance;
            Vec3d vec3EyesBlock = viewEntity.getEyePosition(partialTicks);
            Vec3d vec3ReachBlock = vec3EyesBlock.add(vec3Look.scale((d0 > 100.0D) ? 100.0D : d0));

            for (int c = (int) Math.ceil(d0 / 100.0D); c > 0; c--) {
                objectMouseOver = world.rayTraceBlocks(new RayTraceContext(vec3EyesBlock, vec3ReachBlock, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, viewEntity));
                vec3EyesBlock = vec3ReachBlock;

                if (objectMouseOver != null) {
                    if (objectMouseOver.getType() != RayTraceResult.Type.MISS) {
                        break;
                    } else if (objectMouseOver.getHitVec() != null) {
                        vec3EyesBlock = objectMouseOver.getHitVec();
                    }
                }

                d0 -= 100.0D;
                vec3ReachBlock = vec3ReachBlock.add(vec3Look.scale(Math.min(d0, 100.0D)));
            }

            double d1 = distance;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.getHitVec().distanceTo(vec3Eyes);
            }

            float f = 1.0F;
            Predicate<Entity> predicate = entity -> (entity != null && !entity.isSpectator() && entity.canBeCollidedWith());
            List<Entity> list = world.getEntitiesInAABBexcluding(viewEntity, viewEntity.getBoundingBox().expand(vec3Look.x * d1, vec3Look.y * d1, vec3Look.z * d1).expand(f, f, f), predicate);
            double d2 = d1;

            for (Entity entity1 : list) {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(entity1.getCollisionBorderSize());
                Optional<Vec3d> optional = axisalignedbb.rayTrace(vec3Eyes, vec3Reach);

                if (axisalignedbb.contains(vec3Eyes)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec3EntityPos = optional.orElse(vec3Eyes);
                        d2 = 0.0D;
                    }

                } else if (optional.isPresent()) {
                    double d3 = vec3Eyes.distanceTo(optional.get());

                    if (d3 < d2 || d2 == 0.0D) {
                        if (entity1.getLowestRidingEntity() == viewEntity.getLowestRidingEntity() && !viewEntity.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity1;
                                vec3EntityPos = optional.get();
                            }

                        } else {
                            pointedEntity = entity1;
                            vec3EntityPos = optional.get();
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new EntityRayTraceResult(pointedEntity, vec3EntityPos);
            }
        }

        return objectMouseOver;
    }

    private static final float[] in = new float[4];
    private static final float[] out = new float[4];

    public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        float[] in = ClientUtils.in;
        float[] out = ClientUtils.out;

        in[0] = objx;
        in[1] = objy;
        in[2] = objz;
        in[3] = 1.0f;

        __gluMultMatrixVecf(modelMatrix, in, out);
        __gluMultMatrixVecf(projMatrix, out, in);

        if (in[3] == 0.0)
            return false;

        in[3] = (1.0f / in[3]) * 0.5f;

        in[0] = in[0] * in[3] + 0.5f;
        in[1] = in[1] * in[3] + 0.5f;
        in[2] = in[2] * in[3] + 0.5f;

        win_pos.put(0, in[0] * viewport.get(viewport.position() + 2) + viewport.get(viewport.position()));
        win_pos.put(1, in[1] * viewport.get(viewport.position() + 3) + viewport.get(viewport.position() + 1));
        win_pos.put(2, in[2]);

        return true;
    }

    private static void __gluMultMatrixVecf(FloatBuffer m, float[] in, float[] out) {
        for (int i = 0; i < 4; i++) {
            out[i] =
                    in[0] * m.get(m.position() + i)
                            + in[1] * m.get(m.position() + 4 + i)
                            + in[2] * m.get(m.position() + 8 + i)
                            + in[3] * m.get(m.position() + 12 + i);

        }
    }
}
