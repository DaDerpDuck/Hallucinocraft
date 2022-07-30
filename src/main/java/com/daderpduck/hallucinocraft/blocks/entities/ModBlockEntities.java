package com.daderpduck.hallucinocraft.blocks.entities;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Hallucinocraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<FermentingBottleBlockEntity>> FERMENTING_BOTTLE_BLOCK_ENTITY = register("fermenting_bottle_block_entity", () -> ModBlockEntityTypes.FERMENTING_BOTTLE);

    private static <T extends BlockEntityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return BLOCK_ENTITIES.register(name, supplier);
    }

    public static void register(IEventBus modBus) {
        BLOCK_ENTITIES.register(modBus);
    }
}
