package com.daderpduck.psychedelicraft.client.rendering;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.client.rendering.shaders.RenderUtil;
import com.daderpduck.psychedelicraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.psychedelicraft.drugs.Drug;
import com.daderpduck.psychedelicraft.drugs.DrugEffects;
import com.daderpduck.psychedelicraft.events.hooks.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Psychedelicraft.MOD_ID)
public class DrugRenderer {
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        if (event.phase == TickEvent.Phase.START) {
            Map<Drug, Float> activeDrugs = Drug.getActiveDrugs(mc.player);

            activeDrugs.forEach((drug, effect) -> {
                if (effect > 0) drug.renderTick(effect);
            });
        } else {
            for (DrugEffects effect : DrugEffects.values()) {
                if (effect.isClientOnly()) effect.resetValue();
            }
        }
    }

    private static final CameraTrembleEffect trembleEffect = new CameraTrembleEffect();
    @SubscribeEvent
    public static void onBobHurt(BobHurtEvent event) {
        trembleEffect.setAmplitude(DrugEffects.CAMERA_TREMBLE.getValue());
        trembleEffect.tick(event.matrixStack);
    }

    private static final MouseSmootherEffect smoothEffect = new MouseSmootherEffect();
    @SubscribeEvent
    public static void onPlayerTurn(PlayerTurnEvent event) {
        smoothEffect.tick(DrugEffects.CAMERA_INERTIA.getValue(), event.accumulatedDX, event.accumulatedDY);
    }

    // Shader stuff

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
