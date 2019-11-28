package com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITarget {
    /**
     * Set an entity as the target.
     *
     * @param world The world in which the target is.
     * @param entity An entity which set as the target.
     */
    void setEntity(World world, Entity entity);

    /**
     * Set an position of a block as the target.
     *
     * @param world The world in which the target is.
     * @param x The double value of x position of the target.
     * @param y The double value of y position of the target.
     * @param z The double value of z position of the target.
     */
    void setPos(World world, double x, double y, double z);

    /**
     * Check whether this target is valid.
     *
     * @param world The world in which the target is.
     * @return The boolean whether the target is valid.
     */
    boolean isValid(@Nullable World world);

    /**
     * Get target type.
     *
     * @return Target type.
     */
    Type getType();

    /**
     * Get the target position coordinates.
     *
     * @param world The world in which the target is.
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    Vec3d getPos(@Nullable World world);

    /**
     * Get the target visual position coordinates.
     *
     * @param world The world in which the target is.
     * @param partialTicks The sub-frame fraction.
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    Vec3d getVisualPos(@Nullable World world, float partialTicks);

    /**
     * Get the target motion vector.
     *
     * @param world The world in which the target is.
     * @return A vector containing the double coordinates which is the last tick motion (m/tick) of the target.
     */
    @Nullable
    Vec3d getLastTickMotion(@Nullable World world);

    /**
     * Target types.
     */
    enum Type {
        NONE, BLOCK, ENTITY
    }
}
