package com.github.iunius118.type18ballisticcomputer.client.ballisticcomputer;

import com.github.iunius118.type18ballisticcomputer.config.BallisticComputerConfig;
import net.minecraft.world.World;

public class BallisticComputerSystem {
    public ITracker tracker = new TargetTracker();
    public BallisticComputer computer = new BallisticComputer();
    public IIndicator indicator = new MarkerIndicator();

    public void updateTrackerAndComputer(World world) {
        computer.setBallisticParameters(
                BallisticComputerConfig.ballistic.maxFlightTick,
                BallisticComputerConfig.ballistic.initialVelocity,
                BallisticComputerConfig.ballistic.gravityFactor,
                BallisticComputerConfig.ballistic.resistanceFactor);
        tracker.update(world, computer);
    }

    public void updateIndicator(World world, float partialTicks) {
        indicator.setComputer(computer);
        indicator.update(world, partialTicks);
    }
}
