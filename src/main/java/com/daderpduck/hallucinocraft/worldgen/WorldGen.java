package com.daderpduck.hallucinocraft.worldgen;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hallucinocraft.MOD_ID)
public class WorldGen {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        Biome.BiomeCategory category = event.getCategory();

        if (category == Biome.BiomeCategory.JUNGLE) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.COCA);
        } else if (category == Biome.BiomeCategory.SAVANNA) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.CANNABIS);
        }
    }
}
