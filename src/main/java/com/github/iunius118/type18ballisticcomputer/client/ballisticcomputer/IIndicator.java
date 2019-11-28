package com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IIndicator {
    /**
     * Check whether this indicator is valid.
     *
     * @return The boolean whether the indicator is valid.
     */
    boolean isValid();

    /**
     * Set a computer to this indicator.
     *
     * @param computer A computer to set to the indicator.
     */
    void setComputer(@Nullable IComputer computer);

    /**
     * Get the computer set to this indicator.
     *
     * @return The computer which set to the indicator.
     */
    @Nullable
    IComputer getComputer();

    /**
     * get the target's present position coordinates on screen.
     *
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    Vec3d getTargetScreenPos();

    /**
     * get the target's future position coordinates on screen.
     *
     * @return A vector containing the double coordinates at which the target will be in the future.
     */
    @Nullable
    Vec3d getTargetFutureScreenPos();

    /**
     * calculate a coordinates of target's present and future position on screen.
     *
     * @param world        The world in which the player is.
     * @param partialTicks The sub-frame fraction.
     */
    void update(@Nullable World world, float partialTicks);
}
