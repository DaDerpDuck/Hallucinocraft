package com.daderpduck.hallucinocraft.sounds;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModSounds {
    public static final RegistryObject<SoundEvent> BONG_HIT = register("item.bong_hit", "item.bong_hit");


    private static RegistryObject<SoundEvent> register(String name, String path) {
        return register(name, () -> new SoundEvent(new ResourceLocation(Hallucinocraft.MOD_ID, path)));
    }

    private static RegistryObject<SoundEvent> register(String name, Supplier<SoundEvent> supplier) {
        return Hallucinocraft.SOUNDS.register(name, supplier);
    }

    public static void init() {
    }
}
