package com.brokenkeyboard.usefulspyglass;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ClientConfig {

    public static ForgeConfigSpec.BooleanValue DISPLAY_BLOCKS;

    public static void registerConfig() {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

        DISPLAY_BLOCKS = CONFIG_BUILDER
                .comment("If enabled, block image and name will be displayed")
                .define("Display blocks", true);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_BUILDER.build());
    }
}
