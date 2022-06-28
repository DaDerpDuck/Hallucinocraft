package com.daderpduck.hallucinocraft.blocks;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CutPoppyBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public CutPoppyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter world, BlockPos blockPos) {
        return blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT) || blockState.is(Blocks.COARSE_DIRT) || blockState.is(Blocks.PODZOL) || blockState.is(Blocks.FARMLAND);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return Items.POPPY;
    }

    @Override
    protected int getBonemealAgeIncrease(Level world) {
        return super.getBonemealAgeIncrease(world)/3;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, CollisionContext context) {
        Vec3 vector3d = blockState.getOffset(blockReader, blockPos);
        return SHAPE.move(vector3d.x, vector3d.y, vector3d.z);
    }

    @Override
    public OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }
}
