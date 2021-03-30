package com.daderpduck.psychedelicraft.mixin.client;

import com.daderpduck.psychedelicraft.client.rendering.shaders.ShaderRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Disables overlay textures when world shaders are used
 */
@Mixin(OverlayTexture.class)
public class MixinOverlayTexture {
    @Inject(at = @At("HEAD"), method = "setupOverlayColor", cancellable = true)
    private void onSetup(CallbackInfo ci) {
        if (ShaderRenderer.useShader) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "teardownOverlayColor", cancellable = true)
    private void onTeardown(CallbackInfo ci) {
        if (ShaderRenderer.useShader) ci.cancel();
    }
}
