package com.daderpduck.hallucinocraft.worldgen;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.CannabisBlock;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.blocks.TallCropsBlock;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> COCA = key("coca");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CANNABIS = key("cannabis");

    private static RegistryKey<ConfiguredFeature<?, ?>> key(String name) {
        return RegistryKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(Hallucinocraft.MOD_ID, name));
    }

    @Mod.EventBusSubscriber(modid = Hallucinocraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void register(RegistryEvent.Register<Feature<?>> event) {
            register(COCA, Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.COCA_BLOCK.get().defaultBlockState().setValue(TallCropsBlock.AGE, 3).setValue(TallCropsBlock.CROP, false)), SimpleBlockPlacer.INSTANCE).tries(32).build()).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4));
            register(CANNABIS, Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ModBlocks.CANNABIS_BLOCK.get().defaultBlockState().setValue(CannabisBlock.AGE, 3).setValue(CannabisBlock.CROP, false)), SimpleBlockPlacer.INSTANCE).tries(32).build()).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(24));
        }

        private static void register(RegistryKey<ConfiguredFeature<?, ?>> key, ConfiguredFeature<?, ?> configuredFeature) {
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key.location(), configuredFeature);
        }
    }
}
