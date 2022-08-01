package com.daderpduck.hallucinocraft.recipe;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Hallucinocraft.MOD_ID);
    public static final RegistryObject<RecipeSerializer<FermentingBottleRecipe>> FERMENTING_BOTTLE_SERIALIZER = SERIALIZERS.register("fermenting_bottle", () -> FermentingBottleRecipe.Serializer.INSTANCE);

    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, FermentingBottleRecipe.Type.ID, FermentingBottleRecipe.Type.INSTANCE);
    }

    public static void register(IEventBus modBus) {
        SERIALIZERS.register(modBus);
        modBus.register(ModRecipes.class);
    }
}
