package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.OverlayEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {
//    @Inject(at = @At("TAIL"), method = "_fogMode")
//    private static void onFogMode(int mode, CallbackInfo ci) {
//        MinecraftForge.EVENT_BUS.post(new FogModeEvent(mode));
//    }

    @Inject(at = @At("TAIL"), method = "setupOverlayColor")
    private static void setupOverlay(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new OverlayEvent(true));
    }

    @Inject(at = @At("TAIL"), method = "teardownOverlayColor")
    private static void teardownOverlay(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new OverlayEvent(false));
    }

//    @Inject(at = @At("TAIL"), method = "_enableLighting")
//    private static void enableLighting(CallbackInfo ci) {
//        MinecraftForge.EVENT_BUS.post(new EnableLightEvent(true));
//    }
//
//    @Inject(at = @At("TAIL"), method = "_disableLighting")
//    private static void disableLighting(CallbackInfo ci) {
//        MinecraftForge.EVENT_BUS.post(new EnableLightEvent(false));
//    }
}
