package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.shaders.CompositeRenderTypeExt;
import com.daderpduck.hallucinocraft.events.hooks.BufferDrawEvent;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderType.class) @SuppressWarnings("ConstantConditions")
public class MixinRenderType extends RenderStateShard {
    public MixinRenderType(String name, Runnable setup, Runnable clear) {
        super(name, setup, clear);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;end(Lcom/mojang/blaze3d/vertex/BufferBuilder;)V"), method = "end")
    private void preDraw(BufferBuilder bufferBuilder, int cameraX, int cameraY, int cameraZ, CallbackInfo ci) {
        RenderType renderType = (RenderType) (Object) this;
        AccessorRenderTypeState accessorRenderTypeState = (AccessorRenderTypeState) (Object) ((CompositeRenderTypeExt) renderType).getState();

        InvokerRenderStateTexture invokerRenderStateTexture = (InvokerRenderStateTexture) accessorRenderTypeState.getTextureState();

        ResourceLocation resourceLocation = null;
        if (invokerRenderStateTexture.callCutoutTexture().isPresent()) resourceLocation = invokerRenderStateTexture.callCutoutTexture().get();

        MinecraftForge.EVENT_BUS.post(new BufferDrawEvent.Pre(name, renderType, resourceLocation));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;end(Lcom/mojang/blaze3d/vertex/BufferBuilder;)V", shift = At.Shift.AFTER), method = "end")
    private void postDraw(BufferBuilder bufferBuilder, int cameraX, int cameraY, int cameraZ, CallbackInfo ci) {
        RenderType renderType = (RenderType) (Object) this;
        AccessorRenderTypeState accessorRenderTypeState = (AccessorRenderTypeState) (Object) ((CompositeRenderTypeExt) renderType).getState();

        InvokerRenderStateTexture invokerRenderStateTexture = (InvokerRenderStateTexture) accessorRenderTypeState.getTextureState();

        ResourceLocation resourceLocation = null;
        if (invokerRenderStateTexture.callCutoutTexture().isPresent()) resourceLocation = invokerRenderStateTexture.callCutoutTexture().get();

        MinecraftForge.EVENT_BUS.post(new BufferDrawEvent.Post(name, renderType, resourceLocation));
    }
}
