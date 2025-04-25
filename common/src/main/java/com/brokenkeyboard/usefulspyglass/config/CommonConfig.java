package com.brokenkeyboard.usefulspyglass.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.IntValue MARKING_DURATION;
    public static ForgeConfigSpec.BooleanValue PRECISION_BOWS;
    public static ForgeConfigSpec.IntValue PRECISION_COOLDOWN;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        registerConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    public static void registerConfig(ForgeConfigSpec.Builder builder) {

        MARKING_DURATION = builder
                .comment("The duration of the glowing effect applied by the Marking enchantment. 20 ticks = 1 second.")
                .comment("The cooldown time is the duration multiplied by 1.5.")
                .defineInRange("Marking duration", 200, 200, 600);

        PRECISION_BOWS = builder
                .comment("If enabled, a spyglass with the Precision enchantment can be used with bows.")
                .define("Precision usable with bows", true);

        PRECISION_COOLDOWN = builder
                .comment("The cooldown time caused by using the Precision enchantment. 20 ticks = 1 second.")
                .defineInRange("Precision cooldown", 60, 60, 200);
    }
}