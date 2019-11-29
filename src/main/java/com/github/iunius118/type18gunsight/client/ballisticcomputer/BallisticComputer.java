package com.github.iunius118.type18gunsight.client.ballisticcomputer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BallisticComputer implements IComputer {
    private boolean isValid = false;
    private ITracker tracker;
    private Vec3d futureLoF;

    public float rotationYaw;
    public float rotationPitch;

    private int tickMaxFuse = 100;
    private double initialVelocity = 3.0;
    private double resistance = 0.99;
    private double gravity = 0.05;

    private int tickFuse = 0;   // Fuse tick to set
    private double[] resistanceRates = new double[128];
    private double[] gravityRates = new double[128];

    private static final double MAX_DISTANCE = 256.0;

    public BallisticComputer() {
        updateBallisticParameters(true, true);
    }

    public void setBallisticParameters(int tickMaxFuse, double initialVelocity, double gravity, double resistance) {
        boolean hasGravityChanged = (this.gravity != gravity);
        boolean hasResistanceChanged = (this.resistance != resistance);

        this.tickMaxFuse = tickMaxFuse;
        this.initialVelocity = initialVelocity;
        this.gravity = gravity;
        this.resistance = resistance;
        updateBallisticParameters(hasGravityChanged, hasResistanceChanged);
    }

    private void updateBallisticParameters(boolean hasGravityChanged, boolean hasResistanceChanged) {
        if (hasResistanceChanged) {
            resistanceRates[0] = 0.0;
            resistanceRates[1] = 1.0;

            if (resistance < 0.9999 || resistance > 1.0001) {
                double powResistanceTick = resistance;

                for (int t = 2; t < resistanceRates.length; t++) {
                    powResistanceTick *= resistance;
                    resistanceRates[t] = (resistance - 1.0) / (powResistanceTick - 1.0);
                }
            } else {
                // Resistance is nearly equal to 1
                for (int t = 2; t < resistanceRates.length; t++) {
                    resistanceRates[t] = 1.0 / t;
                }
            }
        }

        if (hasGravityChanged || hasResistanceChanged) {
            gravityRates[0] = 0.0;
            gravityRates[1] = 0.0;

            for (int t = 2; t < gravityRates.length; t++) {
                gravityRates[t] = (t - 1) * gravity + gravityRates[t - 1] * resistance;
            }
        }
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public void setTracker(@Nullable ITracker tracker) {
        this.tracker = tracker;
    }

    @Override
    @Nullable
    public ITracker getTracker() {
        return this.tracker;
    }

    @Override
    public Vec3d getTargetFutureLineOfFire() {
        return this.futureLoF;
    }


    @Override
    public int getFuse() {
        return this.tickFuse;
    }

    @Override
    public void update(@Nullable World world) {
        /*
         * Compute future target direction from the player and the fuse time for EntityThrowable (gravity velocity: 0.03, attenuation rate: 0.99) of which initial velocity is 4 m/ticks.
         */

        if (tracker == null || !tracker.isValid(world)) {
            isValid = false;
            return;
        }

        Vec3d vec3TargetDelta = tracker.getTargetMotion(world);

        if (vec3TargetDelta == null) {
            isValid = false;
            return;
        }

        Entity player = Minecraft.getMinecraft().player;

        if (player == null) {
            isValid = false;
            return;
        }

        Vec3d vec3Player = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d vec3Target = tracker.getTargetPos(world).add(vec3TargetDelta);    // Target position at tick + 1

        int t = 1; // Fuse tick to set

        // Skip tick by distance
        if (this.tracker.getTarget().getType() != ITarget.Type.ENTITY) {
            double r = vec3Player.distanceTo(vec3Target);

            if (r > MAX_DISTANCE) {
                // Out of range
                isValid = false;
                return;
            }
        } else {
            for (; t <= this.tickMaxFuse; t++) {
                double r = vec3Player.distanceTo(vec3Target);

                if (r <= MAX_DISTANCE) {
                    break;
                }

                vec3Target = vec3Target.add(vec3TargetDelta);
            }
        }

        final double v0sq = initialVelocity * initialVelocity;

        for (; t <= tickMaxFuse; t++) {
            // Calculate the projectile's initial velocity to hit the target from time, horizontal distance and height.
            double x1 = vec3Target.x - vec3Player.x;
            double z1 = vec3Target.z - vec3Player.z;
            double tx1 = Math.sqrt(x1 * x1 + z1 * z1);
            double ty1 = vec3Target.y - vec3Player.y;

            double v0x1 = tx1 * getResistanceRate(t - 1);
            double v0y1 = (ty1 + getGravityRate(t - 1)) * getResistanceRate(t - 1);
            double v0sq1 = v0x1 * v0x1 + v0y1 * v0y1;

            double v0x2 = tx1 * getResistanceRate(t);
            double v0y2 = (ty1 + getGravityRate(t)) * getResistanceRate(t);
            double v0sq2 = v0x2 * v0x2 + v0y2 * v0y2;

            if (v0sq1 > v0sq && v0sq2 < v0sq) {
                // If the calculated initial velocity is closest to the real one, update the future target direction from the player and the fuse tick, and return
                futureLoF = new Vec3d(vec3Target.x, vec3Player.y + (v0y1 / v0x1 * tx1), vec3Target.z);
                tickMaxFuse = t;
                isValid = true;

                // For debug
                /*
                rotationYaw = (float)(MathHelper.atan2(-x1, z1) * (180D / Math.PI));
                rotationPitch = -(float)(MathHelper.atan2(v0y1, v0x1) * (180D / Math.PI));
                //*/

                return;
            }

            vec3Target = vec3Target.add(vec3TargetDelta);
        }

        // Out of range
        isValid = false;
    }

    private double getResistanceRate(int tick) {
        if (tick >= 0 && tick < resistanceRates.length) {
            return resistanceRates[tick];
        } else {
            if (resistance < 0.9999 || resistance > 1.0001) {
                return (resistance - 1.0) / (Math.pow(resistance, tick) - 1.0);
            } else {
                // Resistance is nearly equal to 1
                return 1.0 / tick;
            }
        }
    }

    private double getGravityRate(int tick) {
        if (tick >= 0 && tick < gravityRates.length) {
            return gravityRates[tick];
        } else {
            if (resistance < 0.9999 || resistance > 1.0001) {
                return ((Math.pow(resistance, tick - 1) - tick) * resistance + tick - 1) * gravity / Math.pow(1 - resistance, 2);
            } else {
                // Resistance is nearly equal to 1
                return (tick - 1) * gravity * tick / 2 ;
            }
        }
    }
}
