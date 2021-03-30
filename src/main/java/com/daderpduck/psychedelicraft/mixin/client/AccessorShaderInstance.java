package com.daderpduck.psychedelicraft.mixin.client;

import net.minecraft.client.shader.ShaderInstance;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JSONBlendingMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

@Mixin(ShaderInstance.class)
public interface AccessorShaderInstance {
    @Accessor
    Map<String, IntSupplier> getSamplerMap();

    @Accessor
    List<String> getSamplerNames();

    @Accessor
    List<Integer> getSamplerLocations();

    @Accessor
    List<Integer> getUniformLocations();

    @Accessor
    List<ShaderUniform> getUniforms();

    @Accessor
    JSONBlendingMode getBlend();

    @Accessor
    void setDirty(boolean dirty);

    @Accessor
    static void setLastAppliedEffect(ShaderInstance shaderInstance) {
        throw new AssertionError();
    }

    @Accessor
    static void setLastProgramId(int programId) {
        throw new AssertionError();
    }

    @Accessor
    static int getLastProgramId() {
        throw new AssertionError();
    }

    @Invoker
    void callUpdateLocations();
}
