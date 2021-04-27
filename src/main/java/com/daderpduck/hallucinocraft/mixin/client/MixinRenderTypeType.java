package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.shaders.RenderTypeTypeExt;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.renderer.RenderType$Type")
public class MixinRenderTypeType implements RenderTypeTypeExt {
    @Shadow @Final
    private RenderType.State state;

    @Override
    public RenderType.State getState() {
        return state;
    }
}
