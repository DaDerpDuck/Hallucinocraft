package com.daderpduck.psychedelicraft.mixin.client;

import com.daderpduck.psychedelicraft.events.hooks.EnableLightEvent;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks onto when light is toggled
 */
@Mixin(RenderHelper.class)
public class MixinRenderHelper {
    @Inject(at = @At("TAIL"), method = "turnBackOn")
    private static void enableLight(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new EnableLightEvent(true));
    }

    @Inject(at = @At("TAIL"), method = "turnOff")
    private static void disableLight(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new EnableLightEvent(false));
    }
}
