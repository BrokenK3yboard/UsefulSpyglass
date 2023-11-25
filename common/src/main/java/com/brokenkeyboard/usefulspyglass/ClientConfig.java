package com.brokenkeyboard.usefulspyglass;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.BooleanValue DISPLAY_ENTITIES;
    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCKS;
    public static ForgeConfigSpec.IntValue DISPLAY_POSITION;

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

        DISPLAY_POSITION = builder
                .comment("Change the HUD position when viewing blocks/entities. 0 for bottom, 1 for center, 2 for top.")
                .defineInRange("HUD position", 0, 0, 2);
    }
}