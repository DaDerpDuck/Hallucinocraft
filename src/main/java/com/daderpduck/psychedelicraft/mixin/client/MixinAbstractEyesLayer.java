package com.daderpduck.psychedelicraft.mixin.client;

import com.daderpduck.psychedelicraft.events.hooks.RenderEyesEvent;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// unused
@Mixin(AbstractEyesLayer.class)
public class MixinAbstractEyesLayer {
    @Inject(at = @At("HEAD"), method = "render")
    private void renderStart(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEyesEvent(true));
    }

    @Inject(at= @At("TAIL"), method = "render")
    private void renderEnd(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEyesEvent(false));
    }
}
