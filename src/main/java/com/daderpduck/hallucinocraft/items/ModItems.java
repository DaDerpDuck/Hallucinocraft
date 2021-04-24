package com.daderpduck.hallucinocraft.items;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.blocks.ModBlocks;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final RegistryObject<DrugItem> RED_SHROOMS = registerDrug("red_shrooms", new DrugChain().add(DrugRegistry.RED_SHROOMS, 400, 0.3F, 9600));
    public static final RegistryObject<Item> DRIED_RED_MUSHROOM = registerItem("dried_red_mushroom");

    public static final RegistryObject<DrugItem> BROWN_SHROOMS = registerDrug("brown_shrooms", new DrugChain().add(DrugRegistry.BROWN_SHROOMS, 200, 0.3F, 3200));
    public static final RegistryObject<Item> DRIED_BROWN_MUSHROOM = registerItem("dried_brown_mushroom");

    public static final RegistryObject<Item> COKE_CAKE = registerBlock("coke_cake", ModBlocks.COKE_CAKE_BLOCK, 1);
    public static final RegistryObject<Item> COCAINE_ROCK = registerItem("cocaine_rock");
    public static final RegistryObject<DrugItem> COCAINE_POWDER = registerDrug("cocaine_powder", new DrugChain().add(DrugRegistry.COCAINE, 100, 0.3F, 3200), UseAction.BOW, 64);
    public static final RegistryObject<DrugItem> COCAINE_DUST = registerDrug("cocaine_dust", new DrugChain().add(DrugRegistry.COCAINE, 100, 0.05F, 1000), UseAction.BOW, 64);
    public static final RegistryObject<Item> COCA_MULCH = registerItem("coca_mulch");
    public static final RegistryObject<Item> COCA_LEAF = registerItem("coca_leaf");
    public static final RegistryObject<Item> COCA_SEEDS = registerBlockNamed("coca_seeds", ModBlocks.COCA_BLOCK);

    public static final RegistryObject<DrugItem> CANNABIS_JOINT = registerDrug("cannabis_joint", new DrugChain().add(DrugRegistry.CANNABIS, 0, 0.12F, 3200), UseAction.BOW, 16);
    public static final RegistryObject<Item> DRIED_CANNABIS_LEAF = registerItem("dried_cannabis_leaf");
    public static final RegistryObject<Item> CANNABIS_LEAF = registerItem("cannabis_leaf");
    public static final RegistryObject<Item> DRIED_CANNABIS_BUD = registerItem("dried_cannabis_bud");
    public static final RegistryObject<Item> CANNABIS_SEEDS = registerBlockNamed("cannabis_seeds", ModBlocks.CANNABIS_BLOCK);
    public static final RegistryObject<Item> CANNABIS_BUD = registerItem("cannabis_bud");

    public static final RegistryObject<Item> EMPTY_SYRINGE = registerItem("syringe", 16);
    public static final RegistryObject<SyringeItem> COCAINE_SYRINGE = registerSyringe("cocaine_syringe", new DrugChain().add(DrugRegistry.COCAINE, 0, 0.5F, 4800), 0xFFFFFFFF);
    public static final RegistryObject<Item> COKE_CAKE = registerBlock("coke_cake", ModBlocks.COKE_CAKE_BLOCK, 1);

    public static RegistryObject<DrugItem> registerDrug(String name, DrugChain drugChain) {
        return registerDrug(name, drugChain, UseAction.EAT, 64);
    }

    public static RegistryObject<DrugItem> registerDrug(String name, DrugChain drugChain, UseAction useAction, int stackSize) {
        DrugItem.Properties itemProperties = new DrugItem.Properties();
        for (DrugEffectProperty property : drugChain.list) {
            itemProperties.addDrug(property.drug, property.delayTicks, property.potencyPercentage, property.duration);
        }
        return registerItem(name, () -> new DrugItem(itemProperties.useAction(useAction).stacksTo(stackSize).tab(Hallucinocraft.TAB)));
    }

    public static RegistryObject<SyringeItem> registerSyringe(String name, DrugChain drugChain, int color) {
        SyringeItem.Properties itemProperties = new SyringeItem.Properties().color(color);
        for (DrugEffectProperty property : drugChain.list) {
            itemProperties.addDrug(property.drug, property.delayTicks, property.potencyPercentage, property.duration);
        }
        return registerItem(name, () -> new SyringeItem(itemProperties.tab(Hallucinocraft.TAB).stacksTo(1)));
    }

    public static <T extends Block> RegistryObject<Item> registerBlock(String name, RegistryObject<T> block) {
        return registerBlock(name, block, 64);
    }

    public static <T extends Block> RegistryObject<Item> registerBlock(String name, RegistryObject<T> block, int stackSize) {
        return registerItem(name, () -> new BlockItem(block.get(), new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(stackSize)));
    }

    public static <T extends Block> RegistryObject<Item> registerBlockNamed(String name, RegistryObject<T> block) {
        return registerBlockNamed(name, block, 64);
    }

    public static <T extends Block> RegistryObject<Item> registerBlockNamed(String name, RegistryObject<T> block, int stackSize) {
        return registerItem(name, () -> new BlockNamedItem(block.get(), new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(stackSize)));
    }

    public static RegistryObject<Item> registerItem(String name) {
        return registerItem(name, () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB)));
    }

    public static RegistryObject<Item> registerItem(String name, int stackSize) {
        return registerItem(name, () -> new Item(new Item.Properties().tab(Hallucinocraft.TAB).stacksTo(stackSize)));
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier) {
        return Hallucinocraft.ITEMS.register(name, supplier);
    }

    public static class DrugChain {
        private final List<DrugEffectProperty> list = new ArrayList<>();

        public DrugChain add(RegistryObject<Drug> drug, int delayTicks, float potencyPercentage, int duration) {
            list.add(new DrugEffectProperty(drug, delayTicks, potencyPercentage, duration));
            return this;
        }
    }

    private static class DrugEffectProperty {
        private final RegistryObject<Drug> drug;
        private final int delayTicks;
        private final float potencyPercentage;
        private final int duration;

        public DrugEffectProperty(RegistryObject<Drug> drug, int delayTicks, float potencyPercentage, int duration) {
            this.drug = drug;
            this.delayTicks = delayTicks;
            this.potencyPercentage = potencyPercentage;
            this.duration = duration;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register(new SyringeItem.Color(), COCAINE_SYRINGE.get());
        event.getItemColors().register(new BongItem.Color(), BONG.get());
    }

    public static void init() {
    }
}
