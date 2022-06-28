package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.shaders.RenderUtil;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import com.daderpduck.hallucinocraft.events.hooks.SetCameraEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks onto after camera matrix is set, upon chunk layer rendering, upon entity rendering, and upon tile entity rendering
 */
@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @Shadow @Final private RenderBuffers renderBuffers;

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", shift = At.Shift.AFTER), method = "renderLevel")
    private void updateCameraAndRender(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new SetCameraEvent(pPoseStack, pPartialTick, pFinishNanoTime, pRenderBlockOutline, pCamera, pGameRenderer, pLightTexture, pProjectionMatrix));
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", args = {"ldc=filterempty"}), method = "renderChunkLayer")
    private void renderPreChunkLayer(RenderType pRenderType, PoseStack pPoseStack, double pCamX, double pCamY, double pCamZ, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderTerrainEvent(RenderEvent.Phase.START, pRenderType));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER, ordinal = 1), method = "renderChunkLayer")
    private void renderPostChunkLayer(RenderType pRenderType, PoseStack pPoseStack, double pCamX, double pCamY, double pCamZ, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderTerrainEvent(RenderEvent.Phase.END, pRenderType));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;constantAmbientLight()Z"), method = "renderLevel")
    private void renderPostTerrain(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        if (ShaderRenderer.useShader) RenderUtil.flushRenderBuffer();
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = {"ldc=entities"}), method = "renderLevel")
    private void renderPreEntity(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderEntityEvent(RenderEvent.Phase.START, null));
    }

    @Inject(at = @At("HEAD"), method = "renderEntity")
    private void flushEntity(Entity pEntity, double pCamX, double pCamY, double pCamZ, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, CallbackInfo ci) {
        if (ShaderRenderer.useShader) {
            MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderEntityEvent(RenderEvent.Phase.START, pEntity));
            RenderUtil.flushRenderBuffer();
        }
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = {"ldc=blockentities"}), method = "renderLevel")
    private void renderPreBlockEntity(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderEntityEvent(RenderEvent.Phase.END, null));
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderBlockEntityEvent(RenderEvent.Phase.START));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderBuffers;outlineBufferSource()Lnet/minecraft/client/renderer/OutlineBufferSource;", ordinal = 1), method = "renderLevel")
    private void drawBanner(CallbackInfo ci) {
        if (!RenderUtil.hasOptifine) // Optifine already does this
            renderBuffers.bufferSource().endBatch(Sheets.bannerSheet());
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = {"ldc=destroyProgress"}), method = "renderLevel")
    private void renderPostBlockEntity(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderBlockEntityEvent(RenderEvent.Phase.END));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"), method = "renderLevel")
    private void renderPreBlockOutline(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderBlockOutlineEvent(RenderEvent.Phase.START, renderBuffers.bufferSource()));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", shift = At.Shift.AFTER), method = "renderLevel")
    private void renderPostBlockOutline(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderBlockOutlineEvent(RenderEvent.Phase.END, renderBuffers.bufferSource()));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"), method = "renderLevel")
    private void renderPreParticles(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderParticlesEvent(RenderEvent.Phase.START));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V", shift = At.Shift.AFTER), method = "renderLevel")
    private void renderPostParticles(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEvent.RenderParticlesEvent(RenderEvent.Phase.END));
    }
}
