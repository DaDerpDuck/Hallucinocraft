package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Psychedelicraft.MOD_ID)
public class DrugRegistry {
    public static IForgeRegistry<Drug> DRUGS;

    public static final RegistryObject<Drug> RED_SHROOMS = register("red_shrooms", () -> new RedShrooms(new Drug.DrugProperties()));

    public static RegistryObject<Drug> register(String name, Supplier<? extends Drug> supplier) {
        return Psychedelicraft.DRUGS.register(name, supplier);
    }

    @SubscribeEvent
    public static void onNewRegistry(RegistryEvent.NewRegistry event) {
        RegistryBuilder<Drug> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(Psychedelicraft.MOD_ID, "drug"));
        registryBuilder.setType(Drug.class);
        DRUGS = registryBuilder.create();
    }
}
