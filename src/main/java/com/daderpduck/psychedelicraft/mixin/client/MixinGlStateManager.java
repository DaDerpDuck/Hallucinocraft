package com.daderpduck.psychedelicraft.mixin.client;

import com.daderpduck.psychedelicraft.events.hooks.FogModeEvent;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {
    @Inject(at = @At("TAIL"), method = "_fogMode")
    private static void onFogMode(int mode, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new FogModeEvent(mode));
    }
}
