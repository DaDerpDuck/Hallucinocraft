package com.daderpduck.hallucinocraft.items;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SyringeItem extends DrugItem {
    private final int color;

    public SyringeItem(Item.Properties properties) {
        super(properties);
        this.color = ((Properties)properties).color;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack itemStack1 = super.finishUsingItem(itemStack, level, entity);

        if (entity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                if (itemStack1.isEmpty()) return ModItems.EMPTY_SYRINGE.get().getDefaultInstance();

                player.getInventory().add(ModItems.EMPTY_SYRINGE.get().getDefaultInstance());
            }
        }

        return itemStack1;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    public int getColor() {
        return color;
    }

    public static class Properties extends DrugItem.Properties {
        private int color = -1;

        public Properties color(int color) {
            this.color = color;
            return this;
        }
    }

    public static class Color implements ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int tintIndex) {
            return tintIndex == 0 ? ((SyringeItem)itemStack.getItem()).getColor() : -1;
        }
    }
}
