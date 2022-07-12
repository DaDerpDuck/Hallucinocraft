package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.SoundEvent;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Channel.class)
public class MixinChannel {
    @Shadow
    @Final
    private int source;

    private Vec3 pos;

    @Inject(method = "setSelfPosition", at = @At("HEAD"))
    private void setSelfPosition(Vec3 pSource, CallbackInfo ci) {
        pos = pSource;
    }

    @Inject(method = "play", at = @At("HEAD"))
    private void play(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new SoundEvent.Play(source, pos));
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    private void setPitch(float pPitch, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new SoundEvent.SetPitch(source, pos, pPitch)))
            ci.cancel();
    }
}
