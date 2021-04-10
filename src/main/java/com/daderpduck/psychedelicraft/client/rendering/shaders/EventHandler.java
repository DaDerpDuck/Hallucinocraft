package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.events.hooks.RenderBlockEntityEvent;
import com.daderpduck.psychedelicraft.events.hooks.RenderEntityEvent;
import com.daderpduck.psychedelicraft.events.hooks.RenderTerrainEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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

    //TODO: Hook into block outline
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

    @SubscribeEvent
    public static void renderPostWorld(RenderWorldLastEvent event) {
        ShaderRenderer.processPostShaders(event.getPartialTicks());
    }
}
