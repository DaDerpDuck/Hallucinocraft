package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.config.ModConfig;
import com.mojang.blaze3d.audio.Library;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@Mixin(Library.class)
public class MixinLibrary {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lorg/lwjgl/openal/ALC10;alcCreateContext(JLjava/nio/IntBuffer;)J"))
    private long requestAuxSends(long deviceHandle, IntBuffer attrList) {
        if (ModConfig.USE_SOUND_PROCESSOR.get()) {
            return ALC10.alcCreateContext(deviceHandle, new int[]{EXTEfx.ALC_MAX_AUXILIARY_SENDS, ModConfig.MAX_AUX_SENDS.get(), 0, 0});
        } else {
            return ALC10.alcCreateContext(deviceHandle, attrList);
        }
    }
}
