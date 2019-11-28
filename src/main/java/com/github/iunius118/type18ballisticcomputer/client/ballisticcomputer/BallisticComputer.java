package com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BallisticComputer implements IComputer {
    private boolean isValid = false;
    private ITracker tracker;
    private Vec3d futureLoF;

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
            this.isValid = false;
            return;
        }

        Vec3d vec3TargetDelta = tracker.getTargetMotion(world);

        if (vec3TargetDelta == null) {
            this.isValid = false;
            return;
        }

        Vec3d vec3Target = tracker.getTargetPos(world);

        Entity player = Minecraft.getMinecraft().player;

        if (player == null) {
            this.isValid = false;
            return;
        }

        Vec3d vec3Player = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        final double v0sq = this.initialVelocity * this.initialVelocity;

        int t = 0; // Fuse tick to set

        // Skip tick by distance
        if (this.tracker.getTarget().getType() != ITarget.Type.ENTITY) {
            double r = vec3Player.distanceTo(vec3Target);

            if (r > MAX_DISTANCE) {
                // Out of range
                this.isValid = false;
                return;
            }
        } else {
            for (t = 1; t <= this.tickMaxFuse; t++) {
                double r = vec3Player.distanceTo(vec3Target);

                if (r <= MAX_DISTANCE) {
                    break;
                }

                vec3Target = vec3Target.add(vec3TargetDelta);
            }
        }

        // Calculate initial velocity from tick, height and distance
        // TODO:

        for (; t <= tickMaxFuse; t++) {
            // TODO:
        }

        // Out of range
        this.isValid = false;
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
