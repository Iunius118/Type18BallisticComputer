package com.github.iunius118.type18gunsight.client.ballisticcomputer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Target implements ITarget {
    private Type type = Type.NONE;
    private Vec3d pos;
    private int entityId;
    private int worldHashCode;

    public Target(World world, Entity entity) {
        this.setEntity(world, entity);
    }

    public Target(World world, double x, double y, double z) {
        this.setPos(world, x, y, z);
    }

    public Target(World world, Vec3d v) {
        this.setPos(world, v.x, v.y, v.z);
    }

    public Target(World world, RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
            this.setEntity(world, result.entityHit);
        } else if (result.hitVec != null) {
            this.setPos(world, result.hitVec.x, result.hitVec.y, result.hitVec.z);
        }
    }

    @Override
    public void setEntity(World world, Entity entity) {
        Entity targetEntity = entity;

        if (targetEntity instanceof MultiPartEntityPart) {
            IEntityMultiPart parent = ((MultiPartEntityPart) targetEntity).parent;

            if (parent instanceof Entity) {
                targetEntity = (Entity) parent;
            }
        }

        this.type = Type.ENTITY;
        this.entityId = targetEntity.getEntityId();
        this.worldHashCode = world.hashCode();
    }

    private Entity getTargetEntityByID(World world, int id, boolean isDragonBody) {
        Entity entity = world.getEntityByID(this.entityId);

        if (isDragonBody && entity instanceof EntityDragon) {
            return ((EntityDragon) entity).dragonPartBody;
        }

        return entity;
    }

    @Override
    public void setPos(World world, double x, double y, double z) {
        this.type = Type.BLOCK;
        this.pos = new Vec3d(x, y, z);
        this.worldHashCode = world.hashCode();
    }

    @Override
    public boolean isValid(@Nullable World world) {
        if (world == null || this.worldHashCode != world.hashCode()) {
            return false;
        } else if (this.type == Type.ENTITY) {
            Entity entity = getTargetEntityByID(world, this.entityId, true);

            return entity != null && !entity.isDead;
        }

        return true;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    @Nullable
    public Vec3d getPos(@Nullable World world) {
        if (world == null || this.worldHashCode != world.hashCode()) {
            return null;
        } else if (this.type == Type.BLOCK) {
            return this.pos;
        } else if (this.type == Type.ENTITY) {
            Entity entity = getTargetEntityByID(world, this.entityId, true);

            if (entity != null && !entity.isDead) {
                return new Vec3d(entity.posX, entity.posY + entity.height / 2.0D, entity.posZ);
            }
        }

        return null;
    }

    @Override
    @Nullable
    public Vec3d getVisualPos(@Nullable World world, float partialTicks) {
        if (world == null || this.worldHashCode != world.hashCode()) {
            return null;
        } else if (this.type == Type.BLOCK) {
            return this.pos;
        } else if (this.type == Type.ENTITY) {
            return getEntityVisualPos(world, this.entityId, partialTicks);
        }

        return null;
    }

    private Vec3d getEntityVisualPos(World world, int entityId, float partialTicks) {
        Entity entity = getTargetEntityByID(world, this.entityId, false);

        if (entity != null && !entity.isDead) {
            double bx = entity.posX;
            double by = entity.posY;
            double bz = entity.posZ;
            float height = entity.height;

            if (entity instanceof EntityDragon) {
                Entity entityDragonBody = ((EntityDragon) entity).dragonPartBody;

                if (entityDragonBody != null) {
                    bx = entityDragonBody.posX;
                    by = entityDragonBody.posY;
                    bz = entityDragonBody.posZ;
                    height = entityDragonBody.height;
                }
            }

            double x = bx + (entity.lastTickPosX - entity.posX) * (1 - partialTicks);
            double y = by + (entity.lastTickPosY - entity.posY) * (1 - partialTicks) + height / 2.0D;
            double z = bz + (entity.lastTickPosZ - entity.posZ) * (1 - partialTicks);
            return new Vec3d(x, y, z);
        }

        return null;
    }

    @Override
    @Nullable
    public Vec3d getLastTickMotion(@Nullable World world) {
        if (world == null || this.worldHashCode != world.hashCode()) {
            return null;
        } else if (this.type == Type.BLOCK) {
            return new Vec3d(0.0D, 0.0D, 0.0D);
        } else if (this.type == Type.ENTITY) {
            return getEntityPosDelta(world, this.entityId);
        }

        return null;
    }

    private Vec3d getEntityPosDelta(World world, int entityId) {
        Entity entity = getTargetEntityByID(world, entityId, false);

        if (entity != null && !entity.isDead) {
            double x = entity.posX - entity.lastTickPosX;
            double y = entity.posY - entity.lastTickPosY;
            double z = entity.posZ - entity.lastTickPosZ;
            return new Vec3d(x, y, z);
        }

        return null;
    }
}
