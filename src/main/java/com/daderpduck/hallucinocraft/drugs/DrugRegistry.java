package com.daderpduck.hallucinocraft.drugs;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Hallucinocraft.MOD_ID)
public class DrugRegistry {
    public static IForgeRegistry<Drug> DRUGS;

    public static final RegistryObject<Drug> RED_SHROOMS = register("red_shrooms", () -> new RedShrooms(new Drug.DrugProperties().adsr(2400F, 200F, 0.8F, 2400F)));
    public static final RegistryObject<Drug> BROWN_SHROOMS = register("brown_shrooms", () -> new BrownShrooms(new Drug.DrugProperties().adsr(2400F, 0F, 1F, 2400F)));
    public static final RegistryObject<Drug> COCAINE = register("cocaine", () -> new Cocaine(new Drug.DrugProperties().adsr(800F, 0F, 1F, 1200F).max(5)));
    public static final RegistryObject<Drug> CANNABIS = register("cannabis", () -> new Cannabis(new Drug.DrugProperties().adsr(1800F, 0F, 1F, 2400F).max(5)));

    public static RegistryObject<Drug> register(String name, Supplier<? extends Drug> supplier) {
        return Hallucinocraft.DRUGS.register(name, supplier);
    }

    @SubscribeEvent
    public static void onNewRegistry(RegistryEvent.NewRegistry event) {
        RegistryBuilder<Drug> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(Hallucinocraft.MOD_ID, "drug"));
        registryBuilder.setType(Drug.class);
        DRUGS = registryBuilder.create();
    }
}
