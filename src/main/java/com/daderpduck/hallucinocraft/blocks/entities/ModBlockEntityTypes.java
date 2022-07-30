package com.daderpduck.hallucinocraft.blocks.entities;

import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypes {
    public static final BlockEntityType<FermentingBottleBlockEntity> FERMENTING_BOTTLE = BlockEntityType.Builder.of(FermentingBottleBlockEntity::new, ModBlocks.FERMENTING_BOTTLE_BLOCK.get()).build(null);
}
