package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.shaders.CompositeRenderTypeExt;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.renderer.RenderType$CompositeRenderType")
public class MixinCompositeCompositeRenderType implements CompositeRenderTypeExt {
    @Shadow @Final
    private RenderType.CompositeState state;

    @Override
    public RenderType.CompositeState getState() {
        return state;
    }
}
