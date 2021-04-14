package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.daderpduck.hallucinocraft.client.rendering.shaders.PostShaders.*;

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

        try {
            PostShaders.init();
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

        shaderWorld.clear();

        shaderWorld.safeGetUniform("modelViewMat").setMatrix(GlobalUniforms.modelView);
        shaderWorld.safeGetUniform("modelViewInverseMat").setMatrix(GlobalUniforms.modelViewInverse);
        shaderWorld.safeGetUniform("timePassed").setFloat(GlobalUniforms.timePassed);
        shaderWorld.safeGetUniform("fogMode").setInt(GlobalUniforms.fogMode);

        shaderWorld.safeGetUniform("smallWaves").setFloat(DrugEffects.SMALL_WAVES.getClamped());
        shaderWorld.safeGetUniform("bigWaves").setFloat(DrugEffects.BIG_WAVES.getClamped());
        shaderWorld.safeGetUniform("wiggleWaves").setFloat(DrugEffects.WIGGLE_WAVES.getClamped());
        shaderWorld.safeGetUniform("distantWorldDeformation").setFloat(DrugEffects.WORLD_DEFORMATION.getValue());

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
        renderPost(partialTicks);
        RenderSystem.popMatrix();
        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);
    }

    private static void renderPost(float partialTicks) {
        if (DrugEffects.HUE_AMPLITUDE.getValue() > EPSILON) {
            DEPTH.setUniform("Amplitude", (float) (DrugEffects.HUE_AMPLITUDE.getClamped()*Math.PI*2F));
            DEPTH.setUniform("TimePassed", GlobalUniforms.timePassed*2.5F);
            DEPTH.process(partialTicks);
        }

        if (DrugEffects.SATURATION.getValue() != 0) {
            COLOR.setUniform("Saturation", MathHelper.clamp (DrugEffects.SATURATION.getValue() + 1F, 0F, 1.5F));
            COLOR.process(partialTicks);
        }

        if (DrugEffects.BUMPY.getValue() > EPSILON) {
            BUMPY.setUniform("Intensity", DrugEffects.BUMPY.getValue());
            BUMPY.process(partialTicks);
        }

        if (DrugEffects.KALEIDOSCOPE_INTENSITY.getValue() > EPSILON) {
            float value = MathHelper.clamp(DrugEffects.KALEIDOSCOPE_INTENSITY.getValue(), 0F, 0.8F);
            KALEIDOSCOPE2.setUniform("Extend", value);
            KALEIDOSCOPE2.setUniform("Intensity", value + 1F);
            KALEIDOSCOPE2.setUniform("TimePassed", GlobalUniforms.timePassed);
            KALEIDOSCOPE2.setUniform("TimePassedSin", GlobalUniforms.timePassedSin);
            KALEIDOSCOPE2.process(partialTicks);

            KALEIDOSCOPE.setUniform("Extend", value);
            KALEIDOSCOPE.setUniform("Intensity", value + 1F);
            KALEIDOSCOPE.process(partialTicks);
        }
    }

    public static WorldShader getWorldShader() {
        return shaderWorld;
    }

    public static boolean isActive() {
        return activeShader;
    }
}