package com.daderpduck.hallucinocraft.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SnortDrugItem extends DrugItem {
    public SnortDrugItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public UseAction getUseAnimation(ItemStack itemStack) {
        return UseAction.BOW;
    }
}
