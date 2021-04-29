package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class ShaderRenderer {
    public static boolean useShader = false;
    public static boolean isRenderingWorld = false;
    public static boolean lightmapEnable = true;
    private static final Deque<WorldShader> shaderStack = new ArrayDeque<>();
    private static WorldShader shaderWorld;
    private static WorldShader shaderOutlineBox;
    @Nullable
    private static WorldShader activeShader = null;

    public static void setup() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::setup);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        clear(true);
        PostShaders.setup();

        try {
            shaderWorld = new WorldShader(mc.getResourceManager(), "hallucinocraft:world");
            shaderWorld.setSampler("texture", () -> 0);
            shaderWorld.setSampler("lightMap", () -> 2);

            shaderOutlineBox = new WorldShader(mc.getResourceManager(), "hallucinocraft:world_outline");

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

    public static void startRenderPass(@Nullable WorldShader shader) {
        if (!useShader) return;
        if (activeShader == shader) return;
        if (activeShader != null) {
            RenderUtil.flushRenderBuffer();
            activeShader.clear();
        }
        activeShader = shader;
        if (shader == null) return;

        DrugEffects drugEffects = Drug.getDrugEffects();
        shader.clear();

        shader.safeGetUniform("modelViewMat").setMatrix(GlobalUniforms.modelView);
        shader.safeGetUniform("modelViewInverseMat").setMatrix(GlobalUniforms.modelViewInverse);
        shader.safeGetUniform("timePassed").setFloat(GlobalUniforms.timePassed);
        shader.safeGetUniform("fogMode").setInt(GlobalUniforms.fogMode);

        shader.safeGetUniform("smallWaves").setFloat(drugEffects.SMALL_WAVES.getClamped());
        shader.safeGetUniform("bigWaves").setFloat(drugEffects.BIG_WAVES.getClamped());
        shader.safeGetUniform("wiggleWaves").setFloat(drugEffects.WIGGLE_WAVES.getClamped());
        shader.safeGetUniform("distantWorldDeformation").setFloat(drugEffects.WORLD_DEFORMATION.getValue());

        shader.apply();
    }

    public static void processPostShaders(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Framebuffer framebuffer = mc.getMainRenderTarget();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.matrixMode(GL11.GL_TEXTURE);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        PostShaders.processShaders(partialTicks);
        RenderSystem.popMatrix();
        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);
    }

    public static WorldShader getWorldShader() {
        return shaderWorld;
    }

    public static WorldShader getWorldOutlineShader() {
        return shaderOutlineBox;
    }

    public static void pushShader() {
        if (isActive()) shaderStack.addLast(activeShader);
    }

    public static void popShader() {
        if (shaderStack.isEmpty()) return;
        WorldShader shader = shaderStack.pollLast();
        startRenderPass(shader);
    }

    public static boolean isActive() {
        return activeShader != null;
    }

    @Nullable
    public static WorldShader getActiveShader() {
        return activeShader;
    }
}
