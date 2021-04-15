package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.mixin.client.InvokerRenderUtilsOF;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {
    public static final boolean hasOptifine;

    static {
        boolean hasOptifine1;
        try {
            Class.forName("optifine.Utils");
            hasOptifine1 = true;
        } catch (ClassNotFoundException e) {
            hasOptifine1 = false;
        }
        hasOptifine = hasOptifine1;
    }

    public static void flushRenderBuffer() {
        if (hasOptifine) {
            boolean old = InvokerRenderUtilsOF.callSetFlushRenderBuffers(true);
            InvokerRenderUtilsOF.callFlushRenderBuffers();
            InvokerRenderUtilsOF.callSetFlushRenderBuffers(old);
        } else {
            Minecraft mc = Minecraft.getInstance();
            flushRenderBuffer(mc.renderBuffers().bufferSource());
            flushRenderBuffer(mc.renderBuffers().crumblingBufferSource());
        }
    }

    private static void flushRenderBuffer(IRenderTypeBuffer.Impl buffer) {
        RenderTypeBufferExt bufferExt = (RenderTypeBufferExt) buffer;
        bufferExt.flushRenderBuffers();
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
            Hallucinocraft.LOGGER.error(e);
        }
    }
}
