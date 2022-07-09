package com.daderpduck.hallucinocraft.client;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
    public static final boolean HAS_OPTIFINE;

    static {
        boolean flag;
        try {
            Class.forName("optifine.Utils", false, Thread.currentThread().getContextClassLoader());
            flag = true;
        } catch (ClassNotFoundException e) {
            flag = false;
        }
        HAS_OPTIFINE = flag;
    }

    private static long lastGlErrorTime = System.nanoTime();
    public static void checkGlErrors(String location) { // unused
        int i = GlStateManager._getError();
        if (i != GL11.GL_NO_ERROR) {
            String e = getGlErrorString(i);
            if ((System.nanoTime() - lastGlErrorTime) > 10000L) {
                lastGlErrorTime = System.nanoTime();
                Hallucinocraft.LOGGER.error("OpenGL error: {} ({}) at: {}", e, i, location);

                String chatMessage = I18n.get("hallucinocraft.message.glError", e, i, location);
                Minecraft.getInstance().gui.getChat().addMessage(new TextComponent(chatMessage));
            }
        }
    }

    public static String getGlErrorString(int i) {
        return switch (i) {
            case GL11.GL_NO_ERROR -> "No error";
            case GL11.GL_INVALID_ENUM -> "Invalid enum";
            case GL11.GL_INVALID_VALUE -> "Invalid value";
            case GL11.GL_INVALID_OPERATION -> "Invalid operation";
            case GL11.GL_STACK_OVERFLOW -> "Stack overflow";
            case GL11.GL_STACK_UNDERFLOW -> "Stack underflow";
            case GL11.GL_OUT_OF_MEMORY -> "Out of memory";
            case GL30.GL_INVALID_FRAMEBUFFER_OPERATION -> "Invalid framebuffer operation";
            default -> "Unknown";
        };
    }

    private static long lastAlErrorTime = System.nanoTime();
    public static void checkAlErrors(String location) {
        int i = AL11.alGetError();
        if (i != AL11.AL_NO_ERROR) {
            String e = getAlErrorString(i);
            if ((System.nanoTime() - lastAlErrorTime) > 10000L) {
                lastAlErrorTime = System.nanoTime();
                Hallucinocraft.LOGGER.error("OpenAL error: {} ({}) at: {}", e, i, location);

                String chatMessage = I18n.get("hallucinocraft.message.alError", e, i, location);
                Minecraft.getInstance().gui.getChat().addMessage(new TextComponent(chatMessage));
            }
        }
    }

    public static String getAlErrorString(int i) {
        return switch (i) {
            case AL11.AL_INVALID_NAME -> "Invalid name";
            case AL11.AL_INVALID_ENUM -> "Invalid enum";
            case AL11.AL_INVALID_VALUE -> "Invalid value";
            case AL11.AL_INVALID_OPERATION -> "Invalid operation";
            case AL11.AL_OUT_OF_MEMORY -> "Out of memory";
            default -> "Unknown";
        };
    }
}
