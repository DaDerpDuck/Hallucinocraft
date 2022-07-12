package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.audio.SoundProcessor;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class MixinSoundEngine {
    @Inject(method = "loadLibrary", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Listener;reset()V"))
    private void loadLibrary(CallbackInfo ci) {
        SoundProcessor.init();
    }

    @Inject(method = "destroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundEngine;stopAll()V"))
    private void destroy(CallbackInfo ci) {
        SoundProcessor.cleanup();
    }
}
