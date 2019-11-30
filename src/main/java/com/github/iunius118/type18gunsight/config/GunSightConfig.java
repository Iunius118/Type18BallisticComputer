package com.github.iunius118.type18gunsight.config;

import com.github.iunius118.type18gunsight.Type18GunSight;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

@Config(modid = Type18GunSight.MOD_ID)
public class GunSightConfig {
    @Comment("Time between click and shoot, in ticks")
    @LangKey(Type18GunSight.MOD_ID + ".config.tick_time_to_shoot")
    @RangeInt(min = 0, max = 20)
    public static int tickTimeToShoot = 5;

    @Comment("Max flight time of the projectile, in ticks")
    @LangKey(Type18GunSight.MOD_ID + ".config.max_flight_tick")
    @RangeInt(min = 0, max = 100)
    public static int maxFlightTick = 100;

    @Comment("Initial velocity of the projectile, in meters per tick")
    @LangKey(Type18GunSight.MOD_ID + ".config.initial_velocity")
    @RangeDouble(min = 0.0, max = 20.0)
    public static double initialVelocity = 3.0;

    @Comment("Gravity factor")
    @LangKey(Type18GunSight.MOD_ID + ".config.gravity_factor")
    @RangeDouble(min = 0.0, max = 5.0)
    public static double gravityFactor = 0.05;

    @Comment("Resistance factor")
    @LangKey(Type18GunSight.MOD_ID + ".config.resistance_factor")
    @RangeDouble(min = 0.0, max = 2.0)
    public static double resistanceFactor = 0.99;

    @Comment("Color values for the markers on HUD")
    @LangKey(Type18GunSight.MOD_ID + ".config.marker_color")
    public static MarkerColor marker_color = new MarkerColor();

    public static class MarkerColor {
        @Comment("Alpha color value for the markers on HUD")
        @LangKey(Type18GunSight.MOD_ID + ".config.marker_color.alpha")
        @RangeDouble(min = 0.0, max = 1.0)
        public double colorAlpha = 1.0;

        @Comment("Red color value for the markers on HUD")
        @LangKey(Type18GunSight.MOD_ID + ".config.marker_color.red")
        @RangeDouble(min = 0.0, max = 1.0)
        public double colorRed = 0.0;

        @Comment("Green color value for the markers on HUD")
        @LangKey(Type18GunSight.MOD_ID + ".config.marker_color.green")
        @RangeDouble(min = 0.0, max = 1.0)
        public double colorGreen = 1.0;

        @Comment("Blue color value for the markers on HUD")
        @LangKey(Type18GunSight.MOD_ID + ".config.marker_color.blue")
        @RangeDouble(min = 0.0, max = 1.0)
        public double colorBlue = 0.0;
    }
}
