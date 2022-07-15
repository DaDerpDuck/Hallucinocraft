package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.HallucinocraftConfig;
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
        if (HallucinocraftConfig.CLIENT.useSoundProcessor.get()) {
            return ALC10.alcCreateContext(deviceHandle, new int[]{EXTEfx.ALC_MAX_AUXILIARY_SENDS, HallucinocraftConfig.CLIENT.maxAuxSends.get(), 0, 0});
        } else {
            return ALC10.alcCreateContext(deviceHandle, attrList);
        }
    }
}
