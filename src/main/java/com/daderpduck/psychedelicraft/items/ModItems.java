package com.daderpduck.psychedelicraft.items;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    public static final RegistryObject<Item> RED_SHROOMS = Psychedelicraft.ITEMS.register("red_shrooms", () -> new DrugItem(new DrugItem.Properties().tab(ItemGroup.TAB_MATERIALS)));

    public static void register() {}
}
