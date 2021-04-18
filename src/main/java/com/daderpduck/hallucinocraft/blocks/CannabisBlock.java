package com.daderpduck.hallucinocraft.blocks;

import com.daderpduck.hallucinocraft.items.ModItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IItemProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CannabisBlock extends TallCropsBlock {
    public CannabisBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return ModItems.CANNABIS_SEEDS.get();
    }
}
