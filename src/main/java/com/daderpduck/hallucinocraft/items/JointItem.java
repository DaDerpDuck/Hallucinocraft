package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JointItem extends DrugItem {
    public JointItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            PlayerDrugs.getPlayerDrugs(player).setSmokeTicks(4);
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        }

        return super.finishUsingItem(itemStack, level, entity);
    }
}
