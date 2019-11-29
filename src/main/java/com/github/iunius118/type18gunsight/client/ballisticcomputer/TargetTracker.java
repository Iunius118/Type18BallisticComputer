package com.github.iunius118.type18gunsight.client.ballisticcomputer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TargetTracker implements ITracker {
    private ITarget target;

    private static final int MAX_DELTA_COUNT = 8;
    private int deltaPointer = 0;
    private Vec3d[] deltas = new Vec3d[MAX_DELTA_COUNT];

    @Override
    public boolean isValid(@Nullable World world) {
        return this.target != null && target.isValid(world);
    }

    @Override
    public void setTarget(@Nullable ITarget targetIn) {
        this.target = targetIn;
        this.deltas = new Vec3d[MAX_DELTA_COUNT];
    }

    @Override
    @Nullable
    public ITarget getTarget() {
        return this.target;
    }

    @Override
    @Nullable
    public Vec3d getTargetPos(@Nullable World world) {
        if (this.isValid(world)) {
            return this.target.getPos(world);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Vec3d getTargetVisualPos(@Nullable World world, float partialTicks) {
        if (this.isValid(world)) {
            return this.target.getVisualPos(world, partialTicks);
        } else {
            return null;

        }
    }

    @Override
    @Nullable
    public Vec3d getTargetMotion(@Nullable World world) {
        if (!this.isValid(world)) {
            return null;
        }

        if (target.getType() == ITarget.Type.BLOCK) {
            return new Vec3d(0.0D, 0.0D, 0.0D);
        } else if (target.getType() == ITarget.Type.ENTITY) {
            Vec3d vec1 = null;
            Vec3d vec2 = null;
            int deltaCount = 0;

            for (Vec3d delta : this.deltas) {
                if (delta != null) {
                    if (vec1 != null) {
                        vec2 = vec1.add(delta);
                    } else {
                        vec2 = new Vec3d(delta.x, delta.y, delta.z);
                    }

                    vec1 = vec2;
                    deltaCount++;
                }
            }

            if (vec1 != null && deltaCount > 0) {
                return new Vec3d(vec1.x / deltaCount, vec1.y / deltaCount, vec1.z / deltaCount);
            }

            return null;
        } else {
            return null;
        }
    }

    @Override
    public void update(@Nullable World world, IComputer computer) {
        if (this.isValid(world) && target.getType() == ITarget.Type.ENTITY) {
            deltas[deltaPointer] = target.getLastTickMotion(world);

            if (++deltaPointer >= MAX_DELTA_COUNT) {
                deltaPointer = 0;
            }
        }

        computer.setTracker(this);
        computer.update(world);
    }
}
