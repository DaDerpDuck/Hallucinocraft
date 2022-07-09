package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.audio.SoundProcessor;
import com.mojang.blaze3d.audio.Channel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Channel.class)
public class ChannelMixin {
    @Shadow
    @Final
    private int source;

    @Inject(method = "play", at = @At("HEAD"))
    private void play(CallbackInfo ci) {
        SoundProcessor.processSound(source);
    }
}
