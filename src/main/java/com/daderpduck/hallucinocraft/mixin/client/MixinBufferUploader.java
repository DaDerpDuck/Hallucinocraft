package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.ClientUtil;
import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin(BufferUploader.class)
public class MixinBufferUploader {
    @Inject(method = "_end", at = @At(value = "HEAD"))
    private static void onEnd(ByteBuffer pBuffer, VertexFormat.Mode pMode, VertexFormat pFormat, int pVertexCount, VertexFormat.IndexType pIndexType, int pIndexCount, boolean pSequentialIndex, CallbackInfo ci) {
        if (!ClientUtil.HAS_OPTIFINE) MinecraftForge.EVENT_BUS.post(new RenderEvent.BufferUploadShaderEvent());
    }
}
