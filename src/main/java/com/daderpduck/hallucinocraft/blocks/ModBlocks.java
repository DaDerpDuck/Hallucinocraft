package com.daderpduck.hallucinocraft.blocks;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<TallCropsBlock> COCA_BLOCK = register("coca", () -> new TallCropsBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().noCollission().randomTicks().sound(SoundType.CROP)));
    public static final RegistryObject<TallCropsBlock> CANNABIS_BLOCK = register("cannabis", () -> new CannabisBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().noCollission().randomTicks().sound(SoundType.CROP)));
    public static final RegistryObject<CokeCakeBlock> COKE_CAKE_BLOCK = register("coke_cake", () -> new CokeCakeBlock(BlockBehaviour.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL)));
    public static final RegistryObject<CutPoppyBlock> CUT_POPPY_BLOCK = register("cut_poppy", () -> new CutPoppyBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().noCollission().randomTicks().sound(SoundType.GRASS)));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return Hallucinocraft.BLOCKS.register(name, supplier);
    }

    public static void initRenderTypes() {
        ItemBlockRenderTypes.setRenderLayer(COCA_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CANNABIS_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CUT_POPPY_BLOCK.get(), RenderType.cutout());
    }

    public static void init() {
    }
}
