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

import java.io.IOException;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class ShaderRenderer {
    private static final float EPSILON = 1E-6F;
    private static WorldShader shaderWorld;
    private static boolean activeShader = false;
    public static boolean useShader = false;

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
        if (clearPostShaders) PostShaders.cleanup();
        if (shaderWorld != null) shaderWorld.close();
        shaderWorld = null;
    }

    public static void startRenderPass() {
        if (!useShader) return;
        if (activeShader) return;
        activeShader = true;

        DrugEffects drugEffects = Drug.getDrugEffects();
        shaderWorld.clear();

        shaderWorld.safeGetUniform("modelViewMat").setMatrix(GlobalUniforms.modelView);
        shaderWorld.safeGetUniform("modelViewInverseMat").setMatrix(GlobalUniforms.modelViewInverse);
        shaderWorld.safeGetUniform("timePassed").setFloat(GlobalUniforms.timePassed);
        shaderWorld.safeGetUniform("fogMode").setInt(GlobalUniforms.fogMode);

        shaderWorld.safeGetUniform("smallWaves").setFloat(drugEffects.SMALL_WAVES.getClamped());
        shaderWorld.safeGetUniform("bigWaves").setFloat(drugEffects.BIG_WAVES.getClamped());
        shaderWorld.safeGetUniform("wiggleWaves").setFloat(drugEffects.WIGGLE_WAVES.getClamped());
        shaderWorld.safeGetUniform("distantWorldDeformation").setFloat(drugEffects.WORLD_DEFORMATION.getValue());

        shaderWorld.apply();
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

    public static boolean isActive() {
        return activeShader;
    }
}
