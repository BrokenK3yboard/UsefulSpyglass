package com.brokenkeyboard.usefulspyglass.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {

    public static final ModConfigSpec SPEC;
    public static ModConfigSpec.IntValue MARKING_DURATION;
    public static ModConfigSpec.IntValue PRECISION_COOLDOWN;
    public static ModConfigSpec.IntValue SPOTTER_RANGE;
    public static ModConfigSpec.IntValue SPOTTER_DURATION;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        registerConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    public static void registerConfig(ModConfigSpec.Builder builder) {

        MARKING_DURATION = builder
                .comment("The duration of the glowing effect applied by the Marking enchantment. 20 ticks = 1 second.")
                .comment("The cooldown time is the duration multiplied by 0.8.")
                .defineInRange("Marking duration", 300, 200, 600);

        PRECISION_COOLDOWN = builder
                .comment("The cooldown time caused by using the Precision enchantment. 20 ticks = 1 second.")
                .defineInRange("Precision cooldown", 80, 80, 200);

        SPOTTER_RANGE = builder
                .comment("The sight range of Ender eyes summoned by the Spotter enchantment.")
                .defineInRange("Spotter range", 10, 8, 16);

        SPOTTER_DURATION = builder
                .comment("The amount of time Ender eyes summoned by the Spotter enchantment remain active. 20 ticks = 1 second.")
                .comment("The cooldown time is the duration multipled by 1.4.")
                .defineInRange("Spotter duration", 900, 600, 1200);
    }
}