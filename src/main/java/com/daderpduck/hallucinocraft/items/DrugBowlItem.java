package com.daderpduck.hallucinocraft.items;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DrugBowlItem extends DrugItem {
    public DrugBowlItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack newItemstack =  super.finishUsingItem(itemStack, level, entity);
        return entity instanceof Player player && player.getAbilities().instabuild ? newItemstack : new ItemStack(Items.BOWL);
    }
}
