package com.daderpduck.hallucinocraft.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class ModPlacedFeatures {
    public static final Holder<PlacedFeature> COCA = PlacementUtils.register("coca",
            ModConfiguredFeatures.COCA, RarityFilter.onAverageOnceEvery(8),
            InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

    public static final Holder<PlacedFeature> CANNABIS = PlacementUtils.register("cannabis",
            ModConfiguredFeatures.CANNABIS, RarityFilter.onAverageOnceEvery(24),
            InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
}
