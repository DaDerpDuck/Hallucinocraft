package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.shaders.RenderUtil;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.events.hooks.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks onto after camera matrix is set, upon chunk layer rendering, upon entity rendering, and upon tile entity rendering
 */
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", shift = At.Shift.AFTER), method = "renderLevel")
    private void updateCameraAndRender(MatrixStack matrixStackIn, float partialTicks, long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new SetCameraEvent(matrixStackIn, partialTicks, finishTimeNano, drawBlockOutline, activeRenderInfoIn, gameRendererIn, lightmapIn, projectionIn));
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/IProfiler;push(Ljava/lang/String;)V", args = {"ldc=filterempty"}), method = "renderChunkLayer")
    private void renderPreChunkLayer(RenderType renderType, MatrixStack matrixStack, double x, double y, double z, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderTerrainEvent(RenderTerrainEvent.Phase.START, renderType, matrixStack));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;pop()V", shift = At.Shift.AFTER, ordinal = 1), method = "renderChunkLayer")
    private void renderPostChunkLayer(RenderType renderType, MatrixStack matrixStack, double x, double y, double z, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderTerrainEvent(RenderTerrainEvent.Phase.END, renderType, matrixStack));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/DimensionRenderInfo;constantAmbientLight()Z"), method = "renderLevel")
    private void renderPostTerrain(MatrixStack matrixStack, float partialTicks, long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn, CallbackInfo ci) {
        if (ShaderRenderer.useShader) RenderUtil.flushRenderBuffer();
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/IProfiler;popPush(Ljava/lang/String;)V", args = {"ldc=entities"}), method = "renderLevel")
    private void renderPreEntity(MatrixStack matrixStack, float partialTicks, long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEntityEvent(RenderEntityEvent.Phase.START, matrixStack));
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/IProfiler;popPush(Ljava/lang/String;)V", args = {"ldc=blockentities"}), method = "renderLevel")
    private void renderPreBlockEntity(MatrixStack matrixStack, float partialTicks, long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEntityEvent(RenderEntityEvent.Phase.END, matrixStack));
        MinecraftForge.EVENT_BUS.post(new RenderBlockEntityEvent(RenderBlockEntityEvent.Phase.START, matrixStack));
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/IProfiler;popPush(Ljava/lang/String;)V", args = {"ldc=destroyProgress"}), method = "renderLevel")
    private void renderPostBlockEntity(MatrixStack matrixStack, float partialTicks, long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderBlockEntityEvent(RenderBlockEntityEvent.Phase.END, matrixStack));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/renderer/ActiveRenderInfo;FLnet/minecraft/client/renderer/culling/ClippingHelper;)V"), method = "renderLevel")
    private void renderPreParticles(MatrixStack matrixStack, float p_228426_2_, long p_228426_3_, boolean p_228426_5_, ActiveRenderInfo p_228426_6_, GameRenderer p_228426_7_, LightTexture p_228426_8_, Matrix4f p_228426_9_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderParticlesEvent(RenderParticlesEvent.Phase.START, matrixStack));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/renderer/ActiveRenderInfo;FLnet/minecraft/client/renderer/culling/ClippingHelper;)V", shift = At.Shift.AFTER), method = "renderLevel")
    private void renderPostParticles(MatrixStack matrixStack, float p_228426_2_, long p_228426_3_, boolean p_228426_5_, ActiveRenderInfo p_228426_6_, GameRenderer p_228426_7_, LightTexture p_228426_8_, Matrix4f p_228426_9_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderParticlesEvent(RenderParticlesEvent.Phase.END, matrixStack));
    }
}
