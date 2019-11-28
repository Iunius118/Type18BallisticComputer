package com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITracker {
    /**
     * Check whether this tracker is valid.
     *
     * @param world The world in which the player is.
     * @return The boolean whether the tracker is valid.
     */
    boolean isValid(@Nullable World world);


    /**
     * Set a target to this tracker.
     *
     * @param targetIn The target to set.
     */
    void setTarget(@Nullable ITarget targetIn);

    /**
     * Get a target which is being tracked by this tracker.
     *
     * @return The target.
     */
    @Nullable
    ITarget getTarget();

    /**
     * Get the target position coordinates.
     *
     * @param world The world in which the player is.
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    Vec3d getTargetPos(@Nullable World world);

    /**
     * Get the target visual position coordinates.
     *
     * @param world The world in which the player is.
     * @param partialTicks The sub-frame fraction.
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    Vec3d getTargetVisualPos(@Nullable World world, float partialTicks);

    /**
     * Get the target motion vector.
     *
     * @param world The world in which the player is.
     * @return A vector containing the double coordinates which is the motion (m/tick) of the target.
     */
    @Nullable
    Vec3d getTargetMotion(@Nullable World world);

    /**
     * Update this tracker and the computer.
     *
     * @param world The world in which the player is.
     * @param computer The computer linked from this tracker
     */
    void update(@Nullable World world, IComputer computer);
}
