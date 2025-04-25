package com.brokenkeyboard.usefulspyglass.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.BooleanValue DISPLAY_ENTITIES;
    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCKS;
    public static ForgeConfigSpec.DoubleValue HUD_X;
    public static ForgeConfigSpec.DoubleValue HUD_Y;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        registerConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    public static void registerConfig(ForgeConfigSpec.Builder builder) {

        DISPLAY_ENTITIES = builder
                .comment("If enabled, information on entities will be displayed.")
                .define("Display entities", true);

        DISPLAY_BLOCKS = builder
                .comment("If enabled, block image and name will be displayed.")
                .define("Display blocks", true);

        HUD_X = builder
                .comment("Change the horizontal position of the HUD (center alignment). Default 0.09.")
                .defineInRange("Horizontal position", 0.5, 0, 1);

        HUD_Y = builder
                .comment("Change the horizontal position of the HUD. Default 0.09.")
                .defineInRange("Vertical position", 0.05, 0, 1);
    }
}