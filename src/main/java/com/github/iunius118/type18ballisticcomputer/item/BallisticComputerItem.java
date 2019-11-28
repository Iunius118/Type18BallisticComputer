package com.github.iunius118.type18ballisticcomputer.item;

import com.github.iunius118.type18ballisticcomputer.Type18BallisticComputer;
import com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer.ITracker;
import com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer.Target;
import com.github.iunius118.type18ballisticcomputer.client.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public class BallisticComputerItem extends Item {
    private final static double MAX_DISTANCE = 256.0;

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving.world.isRemote && entityLiving == Minecraft.getMinecraft().player) {
            // Targeting process on Client
            RayTraceResult result = ClientUtils.getMouseOver(MAX_DISTANCE, 1.0F);
            ITracker tracker = Type18BallisticComputer.ballisticComputerSystem.tracker;

            if (Minecraft.getMinecraft().player.isSneaking()) {
                // When the player sneaking, release target
                tracker.setTarget(null);

            } else if (result != null && result.typeOfHit != RayTraceResult.Type.MISS) {
                // When ray-tracing founds a target, set it to director
                double d = result.hitVec.squareDistanceTo(entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (d > 36.0D) {
                    // Set the target to director when it is more than 6 meters away
                    tracker.setTarget(new Target(entityLiving.world, result));
                }
            } else {
                // When the player click for sky, release target
                tracker.setTarget(null);
            }
        }

        return super.onEntitySwing(entityLiving, stack);
    }
}
