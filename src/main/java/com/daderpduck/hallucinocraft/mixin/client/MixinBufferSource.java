package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.shaders.RenderTypeBufferExt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(MultiBufferSource.BufferSource.class)
public abstract class MixinBufferSource implements RenderTypeBufferExt {

    @Shadow protected Optional<RenderType> lastState;

    @Shadow public abstract void endBatch();

    @Override
    public void flushRenderBuffers() {
        RenderType renderType = null;
        if (lastState.isPresent()) renderType = lastState.get();
        endBatch();

        if (renderType != null) ((MultiBufferSource.BufferSource)(Object)this).getBuffer(renderType);
    }
}
