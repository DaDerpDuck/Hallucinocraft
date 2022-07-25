package com.daderpduck.hallucinocraft.datagen;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeCrop(ModBlocks.COCA_BLOCK.get(), "coca_stage", "coca_stage");
        getVariantBuilder(ModBlocks.COKE_CAKE_BLOCK.get())
                .partialState().with(CakeBlock.BITES, 0).addModels(new ConfiguredModel(models().withExistingParent("cake","cake")))
                .partialState().with(CakeBlock.BITES, 1).addModels(new ConfiguredModel(models().withExistingParent("cake_slice1","cake_slice1")))
                .partialState().with(CakeBlock.BITES, 2).addModels(new ConfiguredModel(models().withExistingParent("cake_slice2","cake_slice2")))
                .partialState().with(CakeBlock.BITES, 3).addModels(new ConfiguredModel(models().withExistingParent("cake_slice3","cake_slice3")))
                .partialState().with(CakeBlock.BITES, 4).addModels(new ConfiguredModel(models().withExistingParent("cake_slice4","cake_slice4")))
                .partialState().with(CakeBlock.BITES, 5).addModels(new ConfiguredModel(models().withExistingParent("cake_slice5","cake_slice5")))
                .partialState().with(CakeBlock.BITES, 6).addModels(new ConfiguredModel(models().withExistingParent("cake_slice6","cake_slice6")));
        makeCross(ModBlocks.CANNABIS_BLOCK.get(), "cannabis_stage", "cannabis_stage");
        makeCross(ModBlocks.CUT_POPPY_BLOCK.get(), "cut_poppy_stage", "cut_poppy_stage");
    }

    protected void makeCrop(CropBlock block, String modelName, String textureName) {
        getVariantBuilder(block).forAllStates(blockState -> {
            ConfiguredModel[] models = new ConfiguredModel[1];
            models[0] = new ConfiguredModel(models().crop(modelName + blockState.getValue(block.getAgeProperty()),
                    new ResourceLocation(Hallucinocraft.MOD_ID, "block/" + textureName + blockState.getValue(block.getAgeProperty()))));
            return models;
        });
    }

    protected void makeCross(CropBlock block, String modelName, String textureName) {
        getVariantBuilder(block).forAllStates(blockState -> {
            ConfiguredModel[] models = new ConfiguredModel[1];
            models[0] = new ConfiguredModel(models().cross(modelName + blockState.getValue(block.getAgeProperty()),
                    new ResourceLocation(Hallucinocraft.MOD_ID, "block/" + textureName + blockState.getValue(block.getAgeProperty()))));
            return models;
        });
    }


}
