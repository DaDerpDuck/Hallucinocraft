package com.daderpduck.hallucinocraft.client.rendering;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.ClientUtil;
import com.daderpduck.hallucinocraft.client.rendering.shaders.LevelShaders;
import com.daderpduck.hallucinocraft.client.rendering.shaders.post.PostShaders;
import com.daderpduck.hallucinocraft.HallucinocraftConfig;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.events.hooks.BobHurtEvent;
import com.daderpduck.hallucinocraft.mixin.client.InvokerConfigOF;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class DrugRenderer {
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) throws IOException {
        Minecraft mc = Minecraft.getInstance();

        if (!HallucinocraftConfig.CLIENT.useShaders.get()) {
            if (LevelShaders.isSetup()) {
                LevelShaders.cleanup();
                LevelShaders.toggleShaders(false);
            }
            if (PostShaders.isSetup()) {
                PostShaders.cleanup();
                PostShaders.toggleShaders(false);
            }
        } else if (ClientUtil.HAS_OPTIFINE) {
            // Checks for optifine shaders
            if (InvokerConfigOF.callIsShaders()) {
                if (LevelShaders.isSetup()) {
                    LevelShaders.cleanup();
                    LevelShaders.toggleShaders(false);
                }
                if (PostShaders.isSetup()) {
                    PostShaders.cleanup();
                    PostShaders.toggleShaders(false);
                }
            } else {
                if (!LevelShaders.isSetup()) LevelShaders.setup();
                LevelShaders.toggleShaders(HallucinocraftConfig.CLIENT.useLevelShaders.get());
                if (!PostShaders.isSetup()) PostShaders.setup();
                PostShaders.toggleShaders(HallucinocraftConfig.CLIENT.usePostShaders.get());
            }
        } else {
            if (!LevelShaders.isSetup()) LevelShaders.setup();
            LevelShaders.toggleShaders(HallucinocraftConfig.CLIENT.useLevelShaders.get());
            if (!PostShaders.isSetup()) PostShaders.setup();
            PostShaders.toggleShaders(HallucinocraftConfig.CLIENT.usePostShaders.get());
        }

        if (mc.level == null) return;

        if (event.phase == TickEvent.Phase.START) {
            Drug.getDrugEffects().reset(true);
            Map<Drug, Float> activeDrugs = Drug.getActiveDrugs(mc.player);

            activeDrugs.forEach((drug, effect) -> {
                if (effect > 0) drug.renderTick(Drug.getDrugEffects(), effect);
            });

            MouseSmootherEffect.INSTANCE.setAmplifier(Drug.getDrugEffects().CAMERA_INERTIA.getClamped());
        }
    }

    private static final CameraTrembleEffect trembleEffect = new CameraTrembleEffect();
    @SubscribeEvent
    public static void onBobHurt(BobHurtEvent event) {
        trembleEffect.setAmplitude(Drug.getDrugEffects().CAMERA_TREMBLE.getValue());
        trembleEffect.tick(event.matrixStack);
    }

    @SubscribeEvent
    public static void fogDensity(EntityViewRenderEvent.RenderFogEvent event) {
        float fogDensity = Drug.getDrugEffects().FOG_DENSITY.getClamped();
        if (fogDensity > 0) {
            if (event.getNearPlaneDistance() > 0)
                event.scaleNearPlaneDistance(1F + fogDensity*(-2F - 1F));
            event.scaleFarPlaneDistance(1F + fogDensity*(.25F - 1F));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void fogColor(EntityViewRenderEvent.FogColors event) {
        float fogDarken = Drug.getDrugEffects().FOG_DARKEN.getClamped();
        if (fogDarken > 0) {
            event.setRed(Mth.lerp(fogDarken, event.getRed(), 0));
            event.setGreen(Mth.lerp(fogDarken, event.getGreen(), 0));
            event.setBlue(Mth.lerp(fogDarken, event.getBlue(), 0));
        }
    }
}
