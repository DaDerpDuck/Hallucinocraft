package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.audio.SoundProcessor;
import com.daderpduck.hallucinocraft.events.hooks.SoundEvent;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Redirect(method = "calculatePitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getPitch()F"))
    private float getPitch(SoundInstance instance) {
        SoundEvent.SetPitch event = new SoundEvent.SetPitch(new Vec3(instance.getX(), instance.getY(), instance.getZ()), instance.getPitch());
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return event.pitch;
        }
        return instance.getPitch();
    }
}
