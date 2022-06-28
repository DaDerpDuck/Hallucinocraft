package com.daderpduck.hallucinocraft.blocks;

import com.daderpduck.hallucinocraft.items.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CannabisBlock extends TallCropsBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D)};

    public CannabisBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CANNABIS_SEEDS.get();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, CollisionContext context) {
        return SHAPE_BY_AGE[blockState.getValue(getAgeProperty())];
    }
}
