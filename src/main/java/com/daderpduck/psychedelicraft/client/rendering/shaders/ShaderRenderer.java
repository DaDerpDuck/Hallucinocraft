package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.events.hooks.*;
import com.daderpduck.psychedelicraft.mixin.client.RenderTypeBufferAccessor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Psychedelicraft.MOD_ID)
public class ShaderRenderer {
    private static final Matrix4f modelView = new Matrix4f();
    private static final Matrix4f modelViewInverse = new Matrix4f();
    private static final float[] entityColor = new float[4];
    private static WorldShader shaderWorld;
    private static boolean activeShader = false;
    private static boolean lightEnabled = false;
    private static boolean lightmapEnabled = false;
    private static float timePassed = 0;
    public static boolean useShader = true;

    public static void setup() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::setup);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        clear();

        modelView.setIdentity();
        modelViewInverse.setIdentity();

        try {
            shaderWorld = new WorldShader(mc.getResourceManager(), "psychedelicraft:world");
            shaderWorld.setSampler("texture", () -> 0);
            shaderWorld.setSampler("lightMap", () -> 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::clear);
            return;
        }

        if (shaderWorld != null) shaderWorld.close();
        shaderWorld = null;

    }

    public static void startRenderPass(WorldShader shader) {
        if (!useShader) return;
        if (activeShader) return;
        activeShader = true;

        shader.clear();

        shader.safeGetUniform("modelViewMat").setMatrix(modelView);
        shader.safeGetUniform("modelViewInverseMat").setMatrix(modelViewInverse);
        shader.safeGetUniform("timePassed").setFloat(timePassed);

        shader.apply();
    }

    public static void endRenderPass() {
        if (activeShader && !useShader) {
            shaderWorld.clear();
            activeShader = false;
            return;
        }

        if (!activeShader) return;
        activeShader = false;

        shaderWorld.clear();
    }

    public static void checkGlErrors() {
        int i = GlStateManager._getError();
        if (i != GL11.GL_NO_ERROR) {
            String e = "";
            switch (i) {
                case GL11.GL_INVALID_ENUM:
                    e = "invalid enum";
                    break;
                case GL11.GL_INVALID_VALUE:
                    e = "invalid value";
                    break;
                case GL11.GL_INVALID_OPERATION:
                    e = "invalid operation";
                    break;
                case GL11.GL_STACK_OVERFLOW:
                    e = "stack overflow";
                    break;
                case GL11.GL_STACK_UNDERFLOW:
                    e = "stack underflow";
                    break;
                case GL11.GL_OUT_OF_MEMORY:
                    e = "out of memory";
                    break;
            }
            Psychedelicraft.LOGGER.error(e);
        }
    }

    @SubscribeEvent
    public static void onRenderTerrain(RenderTerrainEvent event) {
        if (event.phase == RenderTerrainEvent.Phase.START) {

            shaderWorld.safeGetUniform("lightmapEnabled").setInt(1);
            startRenderPass(shaderWorld);
        } else {
            endRenderPass();
        }

        checkGlErrors();
    }

    @SubscribeEvent
    public static void onRenderEntity(RenderEntityEvent event) {
        if (event.phase == RenderEntityEvent.Phase.START) {
            startRenderPass(shaderWorld);
        } else {
            endRenderPass();
        }

        checkGlErrors();
    }

    @SubscribeEvent
    public static void onRenderBlockEntity(RenderBlockEntityEvent event) {
        if (event.phase == RenderBlockEntityEvent.Phase.START) {
            startRenderPass(shaderWorld);
        } else {
            endRenderPass();
        }

        checkGlErrors();
    }

    // Uniform setters

    @SubscribeEvent
    public static void onRenderStart(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            timePassed = event.renderTickTime + Minecraft.getInstance().gui.getGuiTicks();
        }
    }

    @SubscribeEvent
    public static void onCameraPostSetup(SetCameraEvent event) {
        Matrix4f matrix4f = event.matrixStack.last().pose().copy();
        modelView.set(matrix4f);
        matrix4f.invert();
        modelViewInverse.set(matrix4f);
    }

    @SubscribeEvent
    public static void onEntityColor(EntityColorEvent event) {
        if (entityColor[0] != event.r || entityColor[1] != event.g || entityColor[2] != event.b || entityColor[3] != event.a) {
            entityColor[0] = event.r;
            entityColor[1] = event.g;
            entityColor[2] = event.b;
            entityColor[3] = event.a;

            WorldShaderUniform uniform = shaderWorld.getUniform("entityColor");
            if (uniform != null && activeShader && useShader) {
                flushRenderBuffer();
                uniform.setFloat(entityColor);
                uniform.upload();
            }
        }
    }

    // TODO: Fix invisible mob's eyes not glowing
    @SubscribeEvent
    public static void onLightmapEnable(EnableLightMapEvent event) {
        if (lightmapEnabled != event.enabled) {
            lightmapEnabled = event.enabled;

            WorldShaderUniform uniform = shaderWorld.getUniform("lightmapEnabled");
            if (uniform != null && activeShader && useShader) {
                uniform.setInt(lightmapEnabled ? 1 : 0);
                uniform.upload();
            }
        }
    }

    @SubscribeEvent
    public static void onLightEnable(EnableLightEvent event) {
        if (lightEnabled != event.enabled) {
            lightEnabled = event.enabled;

            WorldShaderUniform uniform = shaderWorld.getUniform("lightEnabled");
            if (uniform != null && activeShader) {
                uniform.setInt(lightEnabled ? 1 : 0);
                uniform.upload();
            }
        }
    }

    /*@SubscribeEvent
    public static void onRenderEyes(RenderEyesEvent event) {
        if (lightmapEnabled != event.enabled) {
            lightmapEnabled = event.enabled;

            WorldShaderUniform uniform = shaderWorld.getUniform("LightEnabled");
            if (uniform != null && activeShader && useShader) {
                uniform.setInt(lightmapEnabled ? 1 : 0);
                uniform.upload();
            }
        }
    }*/

    public static void flushRenderBuffer() {
        Minecraft mc = Minecraft.getInstance();
        flushRenderBuffer(mc.renderBuffers().bufferSource());
        flushRenderBuffer(mc.renderBuffers().crumblingBufferSource());
    }

    private static void flushRenderBuffer(IRenderTypeBuffer.Impl buffer) {
        RenderTypeBufferAccessor accessor = (RenderTypeBufferAccessor) buffer;
        RenderType renderType = null;
        if (accessor.getLastState().isPresent()) {
            renderType = accessor.getLastState().get();
        }
        buffer.endBatch();

        if (renderType != null) {
            buffer.getBuffer(renderType);
        }
    }


    /*
    @SubscribeEvent
    public static void renderPostWorld(RenderWorldLastEvent event) {
        if (shaderInstance == null) return;
        shaderInstance.clear();

        Minecraft mc = Minecraft.getInstance();
        Framebuffer framebuffer = mc.getMainRenderTarget();
        float x = framebuffer.width;
        float y = framebuffer.height;

        framebuffer.bindWrite(false);
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(0,0,500).color(255,255,255,255).endVertex();
        bufferbuilder.vertex(x,0,500).color(255,255,255,255).endVertex();
        bufferbuilder.vertex(x,y,500).color(255,255,255,255).endVertex();
        bufferbuilder.vertex(0,y,500).color(255,255,255,255).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        framebuffer.unbindWrite();

        *//*Minecraft mc = Minecraft.getInstance();
        Framebuffer framebuffer = mc.getMainRenderTarget();

        if (shaderGroup == null) {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), framebuffer, new ResourceLocation("psychedelicraft", "shaders/post/world.json"));
            shaderGroup.resize(width = framebuffer.viewWidth, height = framebuffer.viewHeight);
        } else if (width != framebuffer.viewWidth || height != framebuffer.viewHeight) {
            shaderGroup.resize(width = framebuffer.viewWidth, height = framebuffer.viewHeight);
        }

        RenderSystem.enableTexture();
        RenderSystem.pushMatrix();
        shaderGroup.process(event.getPartialTicks());
        RenderSystem.popMatrix();
        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);*//*
    }*/
}
