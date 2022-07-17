package com.daderpduck.hallucinocraft.items;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties HASH_MUFFIN = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
    public static final FoodProperties CANNABIS_TEA = new FoodProperties.Builder().nutrition(2).saturationMod(1.2F).alwaysEat().build();
    public static final FoodProperties SHROOM_STEW = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
}
