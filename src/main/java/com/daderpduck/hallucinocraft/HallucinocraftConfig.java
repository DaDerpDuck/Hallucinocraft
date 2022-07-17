package com.daderpduck.hallucinocraft;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class HallucinocraftConfig {
    public static final int DEFAULT_MAX_AUX_SENDS = 4;

    public static class Server {
        Server(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Common {
        Common(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue useShaders;
        public final ForgeConfigSpec.BooleanValue useLevelShaders;
        public final ForgeConfigSpec.BooleanValue usePostShaders;
        public final ForgeConfigSpec.BooleanValue useSoundProcessor;
        public final ForgeConfigSpec.IntValue maxAuxSends;

        Client(ForgeConfigSpec.Builder builder) {
            // Shader Configs
            builder.comment("Settings for shaders").push("shader");
            useShaders = builder
                    .comment("Enables use of shaders")
                    .define("use_shaders", true);
            useLevelShaders = builder
                    .comment("Enables use of level shaders (affects block vertices)")
                    .define("use_level_shaders", true);
            usePostShaders = builder
                    .comment("Enables use of post shaders")
                    .define("use_post_shaders", true);
            builder.pop();
            // Sound Configs
            builder.comment("Settings for sound processor").push("sound");
            useSoundProcessor = builder
                    .comment("Enables use of sound processing (restart required)")
                    .define("use_sound_processor", true);
            maxAuxSends = builder
                    .comment("Configures number of max auxiliary send channels (lower values may result in performance boost but limits number of effects) (set to -1 for default value) (restart required)")
                    .defineInRange("max_aux_sends", -1, -1, DEFAULT_MAX_AUX_SENDS);
            builder.pop();
        }
    }

//    static final ForgeConfigSpec serverSpec;
//    public static final HallucinocraftConfig.Server SERVER;
//    static {
//        final Pair<HallucinocraftConfig.Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(HallucinocraftConfig.Server::new);
//        serverSpec = specPair.getRight();
//        SERVER = specPair.getLeft();
//    }

//    static final ForgeConfigSpec commonSpec;
//    public static final HallucinocraftConfig.Common COMMON;
//    static {
//        final Pair<HallucinocraftConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(HallucinocraftConfig.Common::new);
//        commonSpec = specPair.getRight();
//        COMMON = specPair.getLeft();
//    }

    static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        Hallucinocraft.LOGGER.debug("Loaded hallucinocraft config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        Hallucinocraft.LOGGER.debug("Hallucinocraft config just got changed on the file system!");
    }
}
