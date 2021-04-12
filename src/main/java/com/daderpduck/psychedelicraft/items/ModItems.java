package com.daderpduck.psychedelicraft.items;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.drugs.DrugRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final RegistryObject<Item> RED_SHROOMS = register("red_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.RED_SHROOMS, 400, 0.3F, 9600).edible().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> BROWN_SHROOMS = register("brown_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.BROWN_SHROOMS, 200, 0.3F, 3200).edible().tab(ItemGroup.TAB_MATERIALS)));

    public static RegistryObject<Item> register(String name, Supplier<Item> supplier) {
        return Psychedelicraft.ITEMS.register(name, supplier);
    }

    public static void init() {}
}
