package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.events.hooks.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Psychedelicraft.MOD_ID)
public class EventHandler {
    @SubscribeEvent
    public static void onRenderTerrain(RenderTerrainEvent event) {
        if (event.phase == RenderTerrainEvent.Phase.START) {
            ShaderRenderer.getWorldShader().safeGetUniform("lightmapEnabled").setInt(1);
            ShaderRenderer.startRenderPass();
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors();
    }

    @SubscribeEvent
    public static void onRenderEntity(RenderEntityEvent event) {
        if (event.phase == RenderEntityEvent.Phase.START) {
            ShaderRenderer.startRenderPass();
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors();
    }

    @SubscribeEvent
    public static void onRenderBlockEntity(RenderBlockEntityEvent event) {
        if (event.phase == RenderBlockEntityEvent.Phase.START) {
            ShaderRenderer.startRenderPass();
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors();
    }

    /*@SubscribeEvent
    public static void onRenderBlockOutline(RenderBlockOutlineEvent event) {
        if (event.phase == RenderBlockOutlineEvent.Phase.START) {
            RenderUtil.flushRenderBuffer();
            startRenderPass(shaderWorld);
        } else {
            endRenderPass();
        }

        RenderUtil.checkGlErrors();
    }*/

    /*@SubscribeEvent
    public static void renderPostWorld(RenderWorldLastEvent event) {
        if (shaderInstance == null) return;
        shaderInstance.clear();

        Minecraft mc = Minecraft.getInstance();
        Framebuffer framebuffer = mc.getMainRenderTarget();

        if (shaderGroup == null) {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), framebuffer, new ResourceLocation("psychedelicraft", "shaders/post/world.json"));
            shaderGroup.resize(width = framebuffer.viewWidth, height = framebuffer.viewHeight);
        } else if (width != framebuffer.viewWidth || height != framebuffer.viewHeight) {
            shaderGroup.resize(width = framebuffer.viewWidth, height = framebuffer.viewHeight);
        }

        RenderSystem.enableTexture();
        RenderSystem.pushMatrix();
        shaderGroup.process(event.getPartialTicks());
        RenderSystem.popMatrix();
        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);
    }*/
}
