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
    public static final RegistryObject<Block> COCA_BLOCK = register("coca", () -> new TallCropsBlock(AbstractBlock.Properties.of(Material.PLANT).strength(0F).noCollission().randomTicks().sound(SoundType.CROP)));

    public static RegistryObject<Block> register(String name, Supplier<Block> supplier) {
        return Hallucinocraft.BLOCKS.register(name, supplier);
    }

    @OnlyIn(Dist.CLIENT)
    public static void initRenderTypes() {
        RenderTypeLookup.setRenderLayer(ModBlocks.COCA_BLOCK.get(), RenderType.cutout());
    }

    public static void init() {
    }
}
