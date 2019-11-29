package com.github.iunius118.type18gunsight.client.ballisticcomputer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IComputer {
    /**
     * Check whether this computer is valid. If the computed values were not available, this method should return false.
     *
     * @return The boolean whether the computer is valid.
     */
    boolean isValid();

    /**
     * Set a tracker to this computer.
     *
     * @param tracker A tracker to set to the computer.
     */
    void setTracker(@Nullable ITracker tracker);

    /**
     * Get the tracker set to this computer.
     *
     * @return The tracker which set to the computer.
     */
    @Nullable
    ITracker getTracker();

    /**
     * Get the Line of Fire to the target which computed by this computer.
     *
     * @return A vector containing the double coordinates of the Line of Fire to the target.
     */
    Vec3d getTargetFutureLineOfFire();

    /**
     * Get the fuse tick which computed by this computer.
     *
     * @return The fuse time limit in ticks.
     */
    int getFuse();

    /**
     * Update this computer to compute the target's future yaw and pitch.
     *
     * @param world The world in which the player is.
     * @see ITracker#update(World, IComputer)
     */
    void update(@Nullable World world);
}
