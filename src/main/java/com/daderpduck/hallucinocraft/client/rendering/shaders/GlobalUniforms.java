package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.events.hooks.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class GlobalUniforms {
    public static final Matrix4f modelView = new Matrix4f();
    public static final Matrix4f modelViewInverse = new Matrix4f();
    public static float timePassed = 0;
    public static float timePassedSin = 0;
    public static boolean lightMapEnabled = false;
    public static boolean lightEnabled = false;
    public static final float[] entityColor = new float[4];
    public static int fogMode = 9729;

    static {
        modelView.setIdentity();
        modelViewInverse.setIdentity();
    }

    @SubscribeEvent
    public static void onRenderStart(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            timePassed = (event.renderTickTime + Minecraft.getInstance().gui.getGuiTicks())*0.05F;
            timePassedSin = (float) Math.sin(timePassed);
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

            if (!ShaderRenderer.useShader || !ShaderRenderer.isActive()) return;

            WorldShaderUniform uniform = ShaderRenderer.getWorldShader().getUniform("entityColor");
            if (uniform != null) {
                RenderUtil.flushRenderBuffer();
                uniform.setFloat(entityColor);
                uniform.upload();
            }
        }
    }

    // TODO: Fix invisible mob's eyes not glowing
    @SubscribeEvent
    public static void onLightmapEnable(EnableLightMapEvent event) {
        if (lightMapEnabled != event.enabled) {
            lightMapEnabled = event.enabled;

            if (!ShaderRenderer.useShader || !ShaderRenderer.isActive()) return;

            WorldShaderUniform uniform = ShaderRenderer.getWorldShader().getUniform("lightmapEnabled");
            if (uniform != null) {
                uniform.setInt(lightMapEnabled ? 1 : 0);
                uniform.upload();
            }
        }
    }

    @SubscribeEvent
    public static void onLightEnable(EnableLightEvent event) {
        if (lightEnabled != event.enabled) {
            lightEnabled = event.enabled;

            if (!ShaderRenderer.useShader || !ShaderRenderer.isActive()) return;

            WorldShaderUniform uniform = ShaderRenderer.getWorldShader().getUniform("lightEnabled");
            if (uniform != null) {
                uniform.setInt(lightEnabled ? 1 : 0);
                uniform.upload();
            }
        }
    }

    @SubscribeEvent
    public static void onFogMode(FogModeEvent event) {
        fogMode = event.mode;
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

}
