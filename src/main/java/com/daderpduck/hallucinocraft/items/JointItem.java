package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import net.minecraft.MethodsReturnNonnullByDefault;
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
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        if (entity instanceof Player playerEntity) {
            PlayerDrugs.getPlayerDrugs(playerEntity).setSmokeTicks(4);
        }

        return super.finishUsingItem(itemStack, world, entity);
    }
}
