package com.daderpduck.hallucinocraft.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.NBTIngredient;

public class BrewRegistry {
    public static void register() {
        ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
        BrewingRecipeRegistry.addRecipe(NBTIngredient.of(waterBottle), Ingredient.of(ModItems.SOUL_CONCENTRATE.get()), ModItems.SOUL_RESTER_BOTTLE.get().getDefaultInstance());
        BrewingRecipeRegistry.addRecipe(Ingredient.of(ModItems.SOUL_RESTER_BOTTLE.get()), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModItems.SOUL_WRENCHER_BOTTLE.get().getDefaultInstance());
    }
}
