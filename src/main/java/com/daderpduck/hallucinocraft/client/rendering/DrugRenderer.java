package com.daderpduck.hallucinocraft.client.rendering;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.PostShaders;
import com.daderpduck.hallucinocraft.client.rendering.shaders.RenderUtil;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.daderpduck.hallucinocraft.events.hooks.BobHurtEvent;
import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import com.daderpduck.hallucinocraft.mixin.client.InvokerConfigOF;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class DrugRenderer {
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (RenderUtil.hasOptifine) {
            if (InvokerConfigOF.callIsShaders() && ShaderRenderer.useShader) {
                ShaderRenderer.clear(true);
                ShaderRenderer.useShader = false;
                PostShaders.useShaders = false;
            } else if (!InvokerConfigOF.callIsShaders() && !ShaderRenderer.useShader) {
                ShaderRenderer.setup();
                ShaderRenderer.useShader = true;
                PostShaders.useShaders = true;
            }
        }

        if (mc.level == null) return;

        if (event.phase == TickEvent.Phase.START) {
            Map<Drug, Float> activeDrugs = Drug.getActiveDrugs(mc.player);

            activeDrugs.forEach((drug, effect) -> {
                if (effect > 0) drug.renderTick(effect);
            });

            MouseSmootherEffect.INSTANCE.setAmplifier(DrugEffects.CAMERA_INERTIA.getClamped());
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

    // Shader stuff

    @SubscribeEvent
    public static void onRender(RenderEvent event) {
        if (!ShaderRenderer.useShader) return;

        if (event.phase == RenderEvent.Phase.START) {
            if (event instanceof RenderEvent.RenderTerrainEvent) ShaderRenderer.getWorldShader().safeGetUniform("lightmapEnabled").setInt(1);
            ShaderRenderer.startRenderPass();
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors();
    }

    @SubscribeEvent
    public static void renderPostWorld(RenderWorldLastEvent event) {
        if (!PostShaders.useShaders) return;
        ShaderRenderer.processPostShaders(event.getPartialTicks());
    }
}
