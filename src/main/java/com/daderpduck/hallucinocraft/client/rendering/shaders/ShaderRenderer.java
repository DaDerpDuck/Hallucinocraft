package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.client.rendering.shaders.post.PostShaders;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class ShaderRenderer {
    public static boolean useShader = false;

    public static boolean isRenderingWorld = false;
    private static boolean isRenderingLevel = false;

    public static void setup() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::setup);
            return;
        }

        clear(true);

        MinecraftForge.EVENT_BUS.register(GlobalUniforms.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(ShaderEventHandler.class);

        try {
            PostShaders.setup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear(boolean clearPostShaders) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> ShaderRenderer.clear(clearPostShaders));
            return;
        }

        MinecraftForge.EVENT_BUS.unregister(GlobalUniforms.EventHandler.class);
        MinecraftForge.EVENT_BUS.unregister(ShaderEventHandler.class);

        useShader = false;
        isRenderingWorld = false;

        if (clearPostShaders) PostShaders.cleanup();
    }

    public static void processPostShaders(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget framebuffer = mc.getMainRenderTarget();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.resetTextureMatrix();
        PostShaders.processShaders(partialTicks);
        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);
    }

    static class ShaderEventHandler {
        @SubscribeEvent
        public static void onRenderStart(RenderEvent.LevelStartRender event) {
            isRenderingLevel = true;
        }
        @SubscribeEvent
        public static void onRenderLast(RenderLevelLastEvent event) {
            isRenderingLevel = false;
        }

        @SubscribeEvent
        public static void onLevelShader(RenderEvent.LevelShaderEvent event) {
            ShaderInstance shader = RenderSystem.getShader();
            assert shader != null;

            DrugEffects drugEffects = Drug.getDrugEffects();

            shader.safeGetUniform("SmallWaves").set(drugEffects.SMALL_WAVES.getClamped());
            shader.safeGetUniform("BigWaves").set(drugEffects.BIG_WAVES.getClamped());
            shader.safeGetUniform("WiggleWaves").set(drugEffects.WIGGLE_WAVES.getClamped());
            shader.safeGetUniform("DistantWorldDeformation").set(drugEffects.WORLD_DEFORMATION.getValue());
        }

        @SubscribeEvent
        public static void onBufferUpload(RenderEvent.BufferUploadShaderEvent event) {
            if (Minecraft.getInstance().player == null) return;

            ShaderInstance shader = RenderSystem.getShader();
            assert shader != null;

            if (!isRenderingLevel) {
                shader.safeGetUniform("SmallWaves").set(0F);
                shader.safeGetUniform("BigWaves").set(0F);
                shader.safeGetUniform("WiggleWaves").set(0F);
                shader.safeGetUniform("DistantWorldDeformation").set(0F);
                return;
            }

            DrugEffects drugEffects = Drug.getDrugEffects();

            shader.safeGetUniform("SmallWaves").set(drugEffects.SMALL_WAVES.getClamped());
            shader.safeGetUniform("BigWaves").set(drugEffects.BIG_WAVES.getClamped());
            shader.safeGetUniform("WiggleWaves").set(drugEffects.WIGGLE_WAVES.getClamped());
            shader.safeGetUniform("DistantWorldDeformation").set(drugEffects.WORLD_DEFORMATION.getValue());
        }
    }
}
