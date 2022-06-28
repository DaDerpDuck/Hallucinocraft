package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.post.PostShaders;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

@OnlyIn(Dist.CLIENT)
public class ShaderRenderer {
    public static boolean useShader = false;
    public static boolean isRenderingWorld = false;
    public static boolean lightmapEnable = true;
    private static final Deque<ShaderInstance> shaderStack = new ArrayDeque<>();
    private static ShaderInstance shaderWorld;
    private static ShaderInstance shaderOutlineBox;
    @Nullable
    private static ShaderInstance activeShader = null;

    public static void setup() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::setup);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        clear(true);

        MinecraftForge.EVENT_BUS.register(GlobalUniforms.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(ShaderRenderer.EventHandler.class);

        try {
            PostShaders.setup();

            // TODO: Fix world shaders
            shaderWorld = new ShaderInstance(mc.getResourceManager(), new ResourceLocation(Hallucinocraft.MOD_ID, "world"), DefaultVertexFormat.BLOCK);
            shaderWorld.setSampler("texture", 0);
            shaderWorld.setSampler("overlay", 1);
            shaderWorld.setSampler("lightMap", 2);

            shaderOutlineBox = new ShaderInstance(mc.getResourceManager(), new ResourceLocation(Hallucinocraft.MOD_ID, "world_outline"), DefaultVertexFormat.BLOCK);

            useShader = true;
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
        MinecraftForge.EVENT_BUS.unregister(ShaderRenderer.EventHandler.class);

        useShader = false;
        isRenderingWorld = false;
        activeShader = null;
        shaderStack.clear();

        if (clearPostShaders) PostShaders.cleanup();

        if (shaderWorld != null) shaderWorld.close();
        shaderWorld = null;

        if (shaderOutlineBox != null) shaderOutlineBox.close();
        shaderOutlineBox = null;
    }

    public static void startRenderPass(@Nullable ShaderInstance shader) {
        if (activeShader == shader) return;
        if (activeShader != null) {
            RenderUtil.flushRenderBuffer();
            activeShader.clear();
        }
        activeShader = shader;
        if (shader == null) return;

        DrugEffects drugEffects = Drug.getDrugEffects();
        shader.clear();

        shader.safeGetUniform("modelViewMat").set(GlobalUniforms.modelView);
        shader.safeGetUniform("modelViewInverseMat").set(GlobalUniforms.modelViewInverse);
        shader.safeGetUniform("timePassed").set(GlobalUniforms.timePassed);
        shader.safeGetUniform("fogMode").set(GlobalUniforms.fogMode);

        shader.safeGetUniform("smallWaves").set(drugEffects.SMALL_WAVES.getClamped());
        shader.safeGetUniform("bigWaves").set(drugEffects.BIG_WAVES.getClamped());
        shader.safeGetUniform("wiggleWaves").set(drugEffects.WIGGLE_WAVES.getClamped());
        shader.safeGetUniform("distantWorldDeformation").set(drugEffects.WORLD_DEFORMATION.getValue());

        shader.apply();
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

    public static ShaderInstance getWorldShader() {
        return shaderWorld;
    }

    public static ShaderInstance getWorldOutlineShader() {
        return shaderOutlineBox;
    }

    public static void pushShader() {
        if (isActive()) shaderStack.addLast(activeShader);
    }

    public static void popShader() {
        if (shaderStack.isEmpty()) return;
        ShaderInstance shader = shaderStack.pollLast();
        startRenderPass(shader);
    }

    public static boolean isActive() {
        return activeShader != null;
    }

    @Nullable
    public static ShaderInstance getActiveShader() {
        return activeShader;
    }


    // TODO: Get this to work again ples
    static class EventHandler {
//        @SubscribeEvent
//        public static void onTerrain(RenderEvent.RenderTerrainEvent event) {
//            boolean flag = event.blockLayer == RenderType.translucent();
//            if (event.phase == RenderEvent.Phase.START) {
//                ShaderRenderer.lightmapEnable = !flag;
//                ShaderRenderer.isRenderingWorld = true;
//                ShaderRenderer.getWorldShader().safeGetUniform("lightmapEnabled").set(1);
//                ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
//            } else {
//                ShaderRenderer.lightmapEnable = flag;
//            }
//
//            RenderUtil.checkGlErrors("Terrain");
//        }
//
//        @SubscribeEvent
//        public static void onEntity(RenderEvent.RenderEntityEvent event) {
//            if (event.phase == RenderEvent.Phase.START) {
//                ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
//            }
//
//            RenderUtil.checkGlErrors("Entity");
//        }
//
//        @SubscribeEvent
//        public static void onTileEntity(RenderEvent.RenderBlockEntityEvent event) {
//            if (event.phase == RenderEvent.Phase.START) {
//                ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
//            }
//
//            RenderUtil.checkGlErrors("Block entity");
//        }
//
//        @SubscribeEvent
//        public static void onBlockOutline(RenderEvent.RenderBlockOutlineEvent event) {
//            if (event.phase == RenderEvent.Phase.START) {
//                ShaderRenderer.startRenderPass(ShaderRenderer.getWorldOutlineShader());
//            } else {
//                event.buffer.endBatch(RenderType.LINES);
//            }
//
//            RenderUtil.checkGlErrors("Block outline");
//        }
//
//        @SubscribeEvent
//        public static void onParticle(RenderEvent.RenderParticlesEvent event) {
//            if (event.phase == RenderEvent.Phase.START) {
//                ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
//            } else {
//                ShaderRenderer.startRenderPass(null);
//                ShaderRenderer.isRenderingWorld = false;
//            }
//
//            RenderUtil.checkGlErrors("Particles");
//        }
//
//        @SubscribeEvent
//        public static void preDraw(BufferDrawEvent.Pre event) {
//            if (!ShaderRenderer.isRenderingWorld) return;
//            switch (event.name) {
//                case "crumbling", "armor_glint", "armor_entity_glint", "entity_glint", "entity_glint_direct" -> {
//                    ShaderRenderer.pushShader();
//                    ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
//                }
//                case "lightning" -> {
//                    ShaderRenderer.pushShader();
//                    ShaderRenderer.startRenderPass(ShaderRenderer.getWorldOutlineShader());
//                }
//                case "end_portal" -> {
//                    ShaderRenderer.pushShader();
//                    ShaderRenderer.startRenderPass(null);
//                }
//            }
//        }
//
//        @SubscribeEvent
//        public static void postDraw(BufferDrawEvent.Post event) {
//            if (!ShaderRenderer.isRenderingWorld) return;
//            switch (event.name) {
//                case "crumbling" -> {
//                    RenderUtil.checkGlErrors("Block damage");
//                    ShaderRenderer.popShader();
//                }
//                case "armor_glint", "armor_entity_glint", "entity_glint", "entity_glint_direct" -> {
//                    RenderUtil.checkGlErrors("Armor glint");
//                    ShaderRenderer.popShader();
//                }
//                case "lightning" -> {
//                    RenderUtil.checkGlErrors("Lightning");
//                    ShaderRenderer.popShader();
//                }
//                case "end_portal" -> {
//                    RenderUtil.checkGlErrors("End portal");
//                    ShaderRenderer.popShader();
//                }
//            }
//        }
    }
}
