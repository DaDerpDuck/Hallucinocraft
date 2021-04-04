package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.client.rendering.DrugEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class PostShaders {
    private static final float EPSILON = 1E-6F;
    public static PostShader KALEIDOSCOPE;
    public static PostShader KALEIDOSCOPE2;

    public static void init() throws IOException {
        KALEIDOSCOPE = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope.json"));
        KALEIDOSCOPE2 = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope2.json"));
    }

    public static void render(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Framebuffer framebuffer = mc.getMainRenderTarget();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableTexture();
        RenderSystem.matrixMode(GL11.GL_TEXTURE);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        processShaders(partialTicks);
        RenderSystem.popMatrix();
        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);
    }

    private static void processShaders(float partialTicks) {
        if (DrugEffects.KALEIDOSCOPE_INTENSITY.getValue() > EPSILON) {
            float value = DrugEffects.KALEIDOSCOPE_INTENSITY.getValue();
            KALEIDOSCOPE2.setUniform("Extend", value);
            KALEIDOSCOPE2.setUniform("Intensity", value + 1F);
            KALEIDOSCOPE2.setUniform("TimePassed", GlobalUniforms.timePassed);
            KALEIDOSCOPE2.setUniform("TimePassedSin", GlobalUniforms.timePassedSin);
            KALEIDOSCOPE2.process(partialTicks);

            KALEIDOSCOPE.setUniform("Extend", value);
            KALEIDOSCOPE.setUniform("Intensity",   value + 1F);
            KALEIDOSCOPE.process(partialTicks);
        }
    }
}
