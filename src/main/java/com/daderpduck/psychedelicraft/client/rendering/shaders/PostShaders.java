package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.client.rendering.DrugEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class PostShaders {
    private static final float EPSILON = 1E-6F;
    public static PostShader KALEIDOSCOPE;

    public static void init() throws IOException {
        KALEIDOSCOPE = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope.json"));
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
            KALEIDOSCOPE.setUniform("Extend", DrugEffects.KALEIDOSCOPE_INTENSITY.getValue());
            KALEIDOSCOPE.setUniform("Intensity",   DrugEffects.KALEIDOSCOPE_INTENSITY.getValue() + 1F);
            KALEIDOSCOPE.process(partialTicks);
        }
    }
}
