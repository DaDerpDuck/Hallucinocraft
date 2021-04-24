package com.daderpduck.hallucinocraft.blocks;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<TallCropsBlock> COCA_BLOCK = register("coca", () -> new TallCropsBlock(AbstractBlock.Properties.of(Material.PLANT).strength(0F).noCollission().randomTicks().sound(SoundType.CROP)));
    public static final RegistryObject<TallCropsBlock> CANNABIS_BLOCK = register("cannabis", () -> new CannabisBlock(AbstractBlock.Properties.of(Material.PLANT).strength(0F).noCollission().randomTicks().sound(SoundType.CROP)));
    public static final RegistryObject<CokeCakeBlock> COKE_CAKE_BLOCK = register("coke_cake", () -> new CokeCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL)));

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return Hallucinocraft.BLOCKS.register(name, supplier);
    }

    @OnlyIn(Dist.CLIENT)
    public static void initRenderTypes() {
        RenderTypeLookup.setRenderLayer(ModBlocks.COCA_BLOCK.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.CANNABIS_BLOCK.get(), RenderType.cutout());
    }

    public static void init() {
    }
}
