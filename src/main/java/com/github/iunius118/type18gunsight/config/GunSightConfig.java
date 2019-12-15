package com.github.iunius118.type18gunsight.config;

import com.github.iunius118.type18gunsight.Type18GunSight;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Type18GunSight.MOD_ID)
public class GunSightConfig {
    public static int tickTimeToShoot = 5;
    public static int maxFlightTick = 100;
    public static double initialVelocity = 3.0;
    public static double gravityFactor = 0.05;
    public static double resistanceFactor = 0.99;
    public static MarkerColor marker_color = new MarkerColor();

    public static class MarkerColor {
        public double colorAlpha = 1.0;
        public double colorRed = 0.0;
        public double colorGreen = 1.0;
        public double colorBlue = 0.0;
    }

    public static class Client {
        public final IntValue tickTimeToShoot;
        public final IntValue maxFlightTick;
        public final DoubleValue initialVelocity;
        public final DoubleValue gravityFactor;
        public final DoubleValue resistanceFactor;

        public final IntValue markerColor;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client only settings").push("client");
            // client

            tickTimeToShoot = builder
                   .comment("Time between click and shoot, in ticks")
                   .translation(Type18GunSight.MOD_ID + ".configgui.tick_time_to_shoot")
                   .defineInRange("tickTimeToShoot", 5, 0, 20);

            maxFlightTick = builder
                    .comment("Max flight time of the projectile, in ticks")
                    .translation(Type18GunSight.MOD_ID + ".configgui.max_flight_tick")
                    .defineInRange("maxFlightTick", 100, 0, 127);

            initialVelocity = builder
                    .comment("Initial velocity of the projectile, in meters per tick")
                    .translation(Type18GunSight.MOD_ID + ".configgui.initial_velocity")
                    .defineInRange("initialVelocity", 3.0, 0.0, 20.0);

            gravityFactor = builder
                    .comment("Gravity factor")
                    .translation(Type18GunSight.MOD_ID + ".configgui.gravity_factor")
                    .defineInRange("gravityFactor", 0.05, -5.0, 5.0);

            resistanceFactor = builder
                    .comment("Resistance factor")
                    .translation(Type18GunSight.MOD_ID + ".configgui.resistance_factor")
                    .defineInRange("resistanceFactor", 0.99, 0.0, 2.0);


            builder.comment("Color values for the markers on HUD").push("marker_color");
            // client.marker_color

            markerColor = builder
                    .comment("An integer value for the color of markers on HUD, in ARGB8888 format")
                    .translation(Type18GunSight.MOD_ID + ".configgui.marker_color")
                    .defineInRange("markerColor", 0xFF00FF00, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop();

            builder.pop();
        }
    }

    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }


    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            updateClientConfig();
        }
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            updateClientConfig();
        }
    }

    private static void updateClientConfig() {
        tickTimeToShoot = CLIENT.tickTimeToShoot.get();
        maxFlightTick = CLIENT.maxFlightTick.get();
        initialVelocity = CLIENT.initialVelocity.get();
        gravityFactor = CLIENT.gravityFactor.get();
        resistanceFactor = CLIENT.resistanceFactor.get();

        int nColor = CLIENT.markerColor.get();
        marker_color.colorBlue = (nColor & 0xFF) / 255.0;
        nColor >>= 8;
        marker_color.colorGreen = (nColor & 0xFF) / 255.0;
        nColor >>= 8;
        marker_color.colorRed = (nColor & 0xFF) / 255.0;
        nColor >>= 8;
        marker_color.colorAlpha = (nColor & 0xFF) / 255.0;
    }
}
