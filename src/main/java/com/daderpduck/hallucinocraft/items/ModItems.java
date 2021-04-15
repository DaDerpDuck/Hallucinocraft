package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final RegistryObject<Item> RED_SHROOMS = register("red_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.RED_SHROOMS, 400, 0.3F, 9600).edible().tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> BROWN_SHROOMS = register("brown_shrooms", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.BROWN_SHROOMS, 200, 0.3F, 3200).edible().tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> DRIED_RED_MUSHROOM = register("dried_red_mushroom", () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> DRIED_BROWN_MUSHROOM = register("dried_brown_mushroom", () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> COCAINE_POWDER = register("cocaine_powder", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.COCAINE, 100, 0.3F, 3200).edible().useAction(UseAction.BOW).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> COCA_LEAF = register("coca_leaf", () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> COCA_MULCH = register("coca_mulch", () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> COCAINE_DUST = register("cocaine_dust", () -> new DrugItem(new DrugItem.Properties().addDrug(DrugRegistry.COCAINE, 100, 0.05F, 1000).edible().useAction(UseAction.BOW).tab(Hallucinocraft.TAB)));
    public static final RegistryObject<Item> EMPTY_SYRINGE = register("syringe", () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(16)));
    public static final RegistryObject<Item> COCAINE_SYRINGE = register("cocaine_syringe", () -> new SyringeItem(new SyringeItem.Properties().color(0xFFFFFFFF).addDrug(DrugRegistry.COCAINE, 0, 0.5F, 3200).tab(Hallucinocraft.TAB).stacksTo(1)));
    public static final RegistryObject<Item> COCA_SEEDS = register("coca_seeds", () -> new BlockNamedItem(ModBlocks.COCA_BLOCK.get(), new Item.Properties().tab(Hallucinocraft.TAB)));

    public static RegistryObject<Item> register(String name, Supplier<Item> supplier) {
        return Hallucinocraft.ITEMS.register(name, supplier);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register(new SyringeItem.Color(), COCAINE_SYRINGE.get());
    }

    public static void init() {
    }
}
