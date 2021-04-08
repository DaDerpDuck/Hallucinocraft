package com.daderpduck.psychedelicraft.items;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.drugs.DrugRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    public static final RegistryObject<Item> RED_SHROOMS = Psychedelicraft.ITEMS.register("red_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.RED_SHROOMS, 400, 0.3F, 6000).edible().tab(ItemGroup.TAB_MATERIALS)));

    public static void register() {}
}
