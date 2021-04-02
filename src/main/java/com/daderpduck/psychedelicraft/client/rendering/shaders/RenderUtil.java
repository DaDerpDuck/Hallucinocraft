package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.mixin.client.AccessorRenderTypeBuffer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {
    public static void flushRenderBuffer() {
        Minecraft mc = Minecraft.getInstance();
        flushRenderBuffer(mc.renderBuffers().bufferSource());
        flushRenderBuffer(mc.renderBuffers().crumblingBufferSource());
    }

    private static void flushRenderBuffer(IRenderTypeBuffer.Impl buffer) {
        AccessorRenderTypeBuffer accessor = (AccessorRenderTypeBuffer) buffer;
        RenderType renderType = null;
        if (accessor.getLastState().isPresent()) {
            renderType = accessor.getLastState().get();
        }
        buffer.endBatch();

        if (renderType != null) {
            buffer.getBuffer(renderType);
        }
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
}
