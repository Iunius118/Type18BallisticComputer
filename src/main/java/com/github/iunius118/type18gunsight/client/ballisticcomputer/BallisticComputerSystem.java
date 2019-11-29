package com.github.iunius118.type18gunsight.client.ballisticcomputer;

import com.github.iunius118.type18gunsight.config.GunSightConfig;
import net.minecraft.world.World;

public class BallisticComputerSystem {
    public ITracker tracker = new TargetTracker();
    public BallisticComputer computer = new BallisticComputer();
    public IIndicator indicator = new MarkerIndicator();

    public void updateTrackerAndComputer(World world) {
        computer.setBallisticParameters(
                GunSightConfig.maxFlightTick,
                GunSightConfig.initialVelocity,
                GunSightConfig.gravityFactor,
                GunSightConfig.resistanceFactor);
        tracker.update(world, computer);
    }

    public void updateIndicator(World world, float partialTicks) {
        indicator.setComputer(computer);
        indicator.update(world, partialTicks);
    }
}
