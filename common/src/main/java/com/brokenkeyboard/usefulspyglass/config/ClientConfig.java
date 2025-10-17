package com.brokenkeyboard.usefulspyglass.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public static final ModConfigSpec SPEC;
    public static ModConfigSpec.BooleanValue JADE_INTEGRATION;
    public static ModConfigSpec.BooleanValue DISPLAY_ENTITIES;
    public static ModConfigSpec.BooleanValue DISPLAY_BLOCKS;
    public static ModConfigSpec.BooleanValue DISPLAY_NAMESPACE;
    public static ModConfigSpec.DoubleValue HUD_X;
    public static ModConfigSpec.DoubleValue HUD_Y;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        registerConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    public static void registerConfig(ModConfigSpec.Builder builder) {

        JADE_INTEGRATION = builder
                .comment("If enabled and Jade is installed, will use its overlay instead of the built-in overlay.")
                .define("Jade Overlay", true);

        DISPLAY_ENTITIES = builder
                .comment("If enabled, information on entities will be displayed.")
                .define("Display entities", true);

        DISPLAY_BLOCKS = builder
                .comment("If enabled, block image and name will be displayed.")
                .define("Display blocks", true);

        DISPLAY_NAMESPACE = builder
                .comment("If enabled the namespace of blocks and entities will be displayed")
                .define("Display namespace", true);

        HUD_X = builder
                .comment("Change the horizontal position of the HUD (center alignment). Default 0.09.")
                .defineInRange("Horizontal position", 0.5, 0, 1);

        HUD_Y = builder
                .comment("Change the horizontal position of the HUD. Default 0.09.")
                .defineInRange("Vertical position", 0.05, 0, 1);
    }
}