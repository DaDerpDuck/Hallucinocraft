package com.daderpduck.hallucinocraft.mixin.client;

import net.minecraft.client.shader.ShaderUniform;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.Buffer;
import java.nio.IntBuffer;

/**
 * Fixes ShaderUniform incorrectly clearing the float buffer on an integer upload, throwing a NullPointerException
 * Currently unused as another implementation of ShaderUniform is used (see {@link com.daderpduck.hallucinocraft.client.rendering.shaders.WorldShaderUniform})
 */
@Mixin(ShaderUniform.class)
public class MixinShaderUniform {
    @Shadow @Final private IntBuffer intValues;

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/nio/Buffer;clear()Ljava/nio/Buffer;"), method = "uploadAsInteger()V")
    private Buffer fix(Buffer buffer) {
        return intValues.clear();
    }
}
