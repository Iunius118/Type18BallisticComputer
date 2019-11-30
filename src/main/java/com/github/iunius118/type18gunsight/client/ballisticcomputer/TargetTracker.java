package com.github.iunius118.type18gunsight.client.ballisticcomputer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TargetTracker implements ITracker {
    private ITarget target;

    private static final int MAX_POS_COUNT = 8;
    private int posPointer = 0;
    private Vec3d[] posArray = new Vec3d[MAX_POS_COUNT];
    private Vec3d oldestPos;

    @Override
    public boolean isValid(@Nullable World world) {
        return this.target != null && target.isValid(world);
    }

    @Override
    public void setTarget(@Nullable ITarget targetIn) {
        this.target = targetIn;
        this.posArray = new Vec3d[MAX_POS_COUNT];
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
            if (oldestPos != null) {
                Vec3d targetPos = target.getPos(world);

                if (targetPos != null) {
                    return targetPos.subtract(oldestPos).scale(1.0D / MAX_POS_COUNT);
                }
            }

            return null;
        } else {
            return null;
        }
    }

    @Override
    public void update(@Nullable World world, IComputer computer) {
        if (this.isValid(world) && target.getType() == ITarget.Type.ENTITY) {
            Vec3d tmpOldestPos = posArray[posPointer];

            if (tmpOldestPos == null) {
                if (oldestPos == null) {
                    oldestPos = target.getPos(world);
                }
            } else {
                oldestPos = tmpOldestPos;
            }

            posArray[posPointer] = target.getPos(world);

            if (++posPointer >= MAX_POS_COUNT) {
                posPointer = 0;
            }
        }

        computer.setTracker(this);
        computer.update(world);
    }
}
