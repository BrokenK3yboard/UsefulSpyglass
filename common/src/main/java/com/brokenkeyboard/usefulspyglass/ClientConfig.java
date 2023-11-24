package com.brokenkeyboard.usefulspyglass;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.BooleanValue DISPLAY_ENTITIES;
    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCKS;
    public static ForgeConfigSpec.BooleanValue DISPLAY_ENTITY_NAMEPSPACE;
    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCK_NAMEPSPACE;
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

        DISPLAY_ENTITY_NAMEPSPACE = builder
                .comment("If enabled, entity namespace will be displayed.")
                .define("Display entity namespace", true);

        DISPLAY_BLOCK_NAMEPSPACE = builder
                .comment("If enabled, block namespace will be displayed.")
                .define("Display block namespace", true);

        HUD_X = builder
                .comment("Change the horizontal position of the HUD (center alignment). Default 0.09.")
                .defineInRange("Horizontal position", 0.09, 0, 1);

        HUD_Y = builder
                .comment("Change the horizontal position of the HUD. Default 0.09.")
                .defineInRange("Vertical position", 0.09, 0, 1);
    }
}