package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.client.rendering.shaders.post.PostShaders;
import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShaderEventHandler {
    public static class Level {
        private static boolean isRenderingLevel;

        @SubscribeEvent
        public static void onRenderStart(RenderEvent.LevelStartRender event) {
            isRenderingLevel = true;
        }
        @SubscribeEvent
        public static void onRenderLast(RenderLevelLastEvent event) {
            isRenderingLevel = false;
        }

        @SubscribeEvent
        public static void onLevelShader(RenderEvent.LevelShaderEvent event) {
            LevelShaders.setLevelShaderUniforms(isRenderingLevel);
        }

        @SubscribeEvent
        public static void onBufferUpload(RenderEvent.BufferUploadShaderEvent event) {
            LevelShaders.setLevelShaderUniforms(isRenderingLevel);
        }
    }

    public static class Post {
        @SubscribeEvent
        public static void renderPostWorld(RenderLevelLastEvent event) {
            PostShaders.processPostShaders(event.getPartialTick());
        }

        @SubscribeEvent
        public static void onRenderStart(TickEvent.RenderTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                GlobalUniforms.timePassed = (event.renderTickTime + Minecraft.getInstance().gui.getGuiTicks())*0.05F;
                GlobalUniforms.timePassedSin = Mth.sin(GlobalUniforms.timePassed);
            }
        }
    }
}
