package com.daderpduck.hallucinocraft.blocks;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CokeCakeBlock extends CakeBlock {
    public CokeCakeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player playerEntity, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        InteractionResult resultType = super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
        if (resultType.consumesAction()) Drug.addDrug(playerEntity, new DrugInstance(DrugRegistry.COCAINE.get(), 800, 0.2F, 3200));
        return resultType;
    }
}
