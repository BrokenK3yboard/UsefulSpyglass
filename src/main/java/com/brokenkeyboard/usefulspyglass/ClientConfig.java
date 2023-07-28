package com.brokenkeyboard.usefulspyglass;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ClientConfig {

    public static ForgeConfigSpec.BooleanValue DISPLAY_ENTITIES;
    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCKS;
    public static ForgeConfigSpec.BooleanValue DISPLAY_ENTITY_NAMEPSPACE;
    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCK_NAMEPSPACE;
    public static ForgeConfigSpec.DoubleValue HUD_X;
    public static ForgeConfigSpec.DoubleValue HUD_Y;

    public static void registerConfig() {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

        DISPLAY_ENTITIES = CONFIG_BUILDER
                .comment("If enabled, information on entities will be displayed.")
                .define("Display entities", true);

        DISPLAY_BLOCKS = CONFIG_BUILDER
                .comment("If enabled, block image and name will be displayed.")
                .define("Display blocks", true);

        DISPLAY_ENTITY_NAMEPSPACE = CONFIG_BUILDER
                .comment("If enabled, entity namespace will be displayed.")
                .define("Display entity namespace", true);

        DISPLAY_BLOCK_NAMEPSPACE = CONFIG_BUILDER
                .comment("If enabled, block namespace will be displayed.")
                .define("Display block namespace", true);

        HUD_X = CONFIG_BUILDER
                .comment("Change the horizontal position of the HUD (center alignment). Default 0.09.")
                .defineInRange("Horizontal position", 0.09, 0, 1);

        HUD_Y = CONFIG_BUILDER
                .comment("Change the horizontal position of the HUD. Default 0.09.")
                .defineInRange("Vertical position", 0.09, 0, 1);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_BUILDER.build());
    }
}