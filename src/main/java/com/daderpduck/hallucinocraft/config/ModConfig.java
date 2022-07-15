package com.daderpduck.hallucinocraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig {
    // Client Configs
    public static ForgeConfigSpec.BooleanValue USE_SHADERS;
    public static ForgeConfigSpec.BooleanValue USE_LEVEL_SHADERS;
    public static ForgeConfigSpec.BooleanValue USE_POST_SHADERS;

    public static ForgeConfigSpec.BooleanValue USE_SOUND_PROCESSOR;
    public static ForgeConfigSpec.IntValue MAX_AUX_SENDS;
    // Common Configs

    // Server Configs

    public static void register() {
        registerServerConfigs();
        registerCommonConfigs();
        registerClientConfigs();
    }

    private static void registerClientConfigs() {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        // Shader Configs
        CLIENT_BUILDER.comment("Settings for shaders").push("shader");
        USE_SHADERS = CLIENT_BUILDER
                .comment("Enables use of shaders")
                .define("use_shaders", true);
        USE_LEVEL_SHADERS = CLIENT_BUILDER
                .comment("Enables use of level shaders (affects block vertices)")
                .define("use_level_shaders", true);
        USE_POST_SHADERS = CLIENT_BUILDER
                .comment("Enables use of post shaders")
                .define("use_post_shaders", true);
        CLIENT_BUILDER.pop();
        // Sound Configs
        CLIENT_BUILDER.comment("Settings for sound processor").push("sound");
        USE_SOUND_PROCESSOR = CLIENT_BUILDER
                .comment("Enables use of sound processing (restart required)")
                .define("use_sound_processor", true);
        MAX_AUX_SENDS = CLIENT_BUILDER
                .comment("Configures number of max auxiliary send channels (lower values may result in performance boost but limits number of effects) (restart required)")
                .defineInRange("max_aux_sends", 2, 0, 2);
        CLIENT_BUILDER.pop();

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {

    }

    private static void registerServerConfigs() {

    }
}
