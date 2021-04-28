package com.daderpduck.hallucinocraft.client.rendering;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.PostShaders;
import com.daderpduck.hallucinocraft.client.rendering.shaders.RenderUtil;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.events.hooks.BobHurtEvent;
import com.daderpduck.hallucinocraft.events.hooks.BufferDrawEvent;
import com.daderpduck.hallucinocraft.events.hooks.RenderEvent;
import com.daderpduck.hallucinocraft.mixin.client.InvokerConfigOF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
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
                if (effect > 0) drug.renderTick(Drug.getDrugEffects(), effect);
            });

            MouseSmootherEffect.INSTANCE.setAmplifier(Drug.getDrugEffects().CAMERA_INERTIA.getClamped());
        } else {
            Drug.getDrugEffects().reset(true);
        }
    }

    private static final CameraTrembleEffect trembleEffect = new CameraTrembleEffect();
    @SubscribeEvent
    public static void onBobHurt(BobHurtEvent event) {
        trembleEffect.setAmplitude(Drug.getDrugEffects().CAMERA_TREMBLE.getValue());
        trembleEffect.tick(event.matrixStack);
    }

    // Shader stuff

    @SubscribeEvent
    public static void onTerrain(RenderEvent.RenderTerrainEvent event) {
        if (event.phase == RenderEvent.Phase.START) {
            ShaderRenderer.getWorldShader().safeGetUniform("lightmapEnabled").setInt(1);
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors("Terrain");
    }

    @SubscribeEvent
    public static void onEntity(RenderEvent.RenderEntityEvent event) {
        if (event.phase == RenderEvent.Phase.START) {
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors("Entity");
    }

    @SubscribeEvent
    public static void onTileEntity(RenderEvent.RenderBlockEntityEvent event) {
        if (event.phase == RenderEvent.Phase.START) {
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors("Block entity");
    }

    @SubscribeEvent
    public static void onBlockOutline(RenderEvent.RenderBlockOutlineEvent event) {
        if (event.phase == RenderEvent.Phase.START) {
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldOutlineShader());
        } else {
            event.buffer.endBatch(RenderType.LINES);
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors("Block outline");
    }

    @SubscribeEvent
    public static void onParticle(RenderEvent.RenderParticlesEvent event) {
        if (event.phase == RenderEvent.Phase.START) {
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
        } else {
            ShaderRenderer.endRenderPass();
        }

        RenderUtil.checkGlErrors("Particles");
    }

    @SubscribeEvent
    public static void preDraw(BufferDrawEvent.Pre event) {
        if (!ShaderRenderer.useShader) return;
        if (event.name.equals("crumbling")) {
            ShaderRenderer.pushShader();
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
        } else if (event.name.equals("armor_glint") || event.name.equals("armor_entity_glint") || event.name.equals("entity_glint") || event.name.equals("entity_glint_direct")) {
            ShaderRenderer.pushShader();
            ShaderRenderer.startRenderPass(ShaderRenderer.getWorldShader());
        }
    }

    @SubscribeEvent
    public static void postDraw(BufferDrawEvent.Post event) {
        if (!ShaderRenderer.useShader) return;
        if (event.name.equals("crumbling")) {
            ShaderRenderer.endRenderPass();
            RenderUtil.checkGlErrors("Block damage");
            ShaderRenderer.popShader();
        } else if (event.name.equals("armor_glint") || event.name.equals("armor_entity_glint") || event.name.equals("entity_glint") || event.name.equals("entity_glint_direct")) {
            ShaderRenderer.endRenderPass();
            RenderUtil.checkGlErrors("Armor glint");
            ShaderRenderer.popShader();
        }
    }

    @SubscribeEvent
    public static void renderPostWorld(RenderWorldLastEvent event) {
        if (!PostShaders.useShaders) return;
        ShaderRenderer.processPostShaders(event.getPartialTicks());
    }
}
