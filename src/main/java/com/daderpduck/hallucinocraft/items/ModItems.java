package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ModItems {
    public static final RegistryObject<Item> RED_SHROOMS = register("red_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.RED_SHROOMS, 400, 0.3F, 9600).edible().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> BROWN_SHROOMS = register("brown_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.BROWN_SHROOMS, 200, 0.3F, 3200).edible().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> DRIED_RED_MUSHROOM = register("dried_red_mushroom", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> DRIED_BROWN_MUSHROOM = register("dried_brown_mushroom", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> COCAINE_POWDER = register("cocaine_powder", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.COCAINE_POWDER, 100, 0.6F, 3200).edible().tab(ItemGroup.TAB_MATERIALS)) {
        @Nonnull
        @Override
        public UseAction getUseAnimation(@Nonnull ItemStack itemStack) {
            return UseAction.BOW;
        }
    });

    public static RegistryObject<Item> register(String name, Supplier<Item> supplier) {
        return Hallucinocraft.ITEMS.register(name, supplier);
    }

    public static void init() {}
}
