package com.daderpduck.hallucinocraft.items;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class CauldronRegistry {
    public static void register() {
        CauldronInteraction.WATER.put(ModItems.BONG.get(), (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide && itemStack.getDamageValue() > 0) {
                itemStack.setDamageValue(0);
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        });
    }
}
