package com.github.iunius118.type18gunsight.item;

import com.github.iunius118.type18gunsight.Type18GunSight;
import com.github.iunius118.type18gunsight.client.ballisticcomputer.ITracker;
import com.github.iunius118.type18gunsight.client.ballisticcomputer.Target;
import com.github.iunius118.type18gunsight.client.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class GunSightItem extends Item {
    private final static double MAX_DISTANCE = 256.0;

    public GunSightItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.world.isRemote && entity == Minecraft.getInstance().player) {
            // Targeting process on Client
            RayTraceResult result = ClientUtils.getMouseOver(MAX_DISTANCE, 1.0F);
            ITracker tracker = Type18GunSight.ballisticComputerSystem.tracker;

            if (Minecraft.getInstance().player.isSneaking()) {
                // When the player sneaking, release target
                tracker.setTarget(null);

            } else if (result != null && result.getType() != RayTraceResult.Type.MISS) {
                // When ray-tracing founds a target, set it to director
                double d = result.getHitVec().squareDistanceTo(entity.posX, entity.posY, entity.posZ);

                if (d > 36.0D) {
                    // Set the target to director when it is more than 6 meters away
                    tracker.setTarget(new Target(entity.world, result));
                }
            } else {
                // When the player click for sky, release target
                tracker.setTarget(null);
            }
        }

        return super.onEntitySwing(stack, entity);
    }
}
