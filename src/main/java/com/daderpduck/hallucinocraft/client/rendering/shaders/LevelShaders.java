package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.common.MinecraftForge;

public class LevelShaders {
    private static boolean useLevelShaders = false;
    private static boolean isSetup = false;

    public static void setup() {
        MinecraftForge.EVENT_BUS.register(ShaderEventHandler.Level.class);

        isSetup = true;
    }

    public static void cleanup() {
        MinecraftForge.EVENT_BUS.unregister(ShaderEventHandler.Level.class);

        isSetup = false;
    }

    public static void toggleShaders(boolean useShaders) {
        useLevelShaders = useShaders;
    }

    public static boolean isSetup() {
        return isSetup;
    }

    public static void setLevelShaderUniforms(boolean isRenderingLevel) {
        if (!useLevelShaders) return; // TODO: Test if this is safe
        if (Minecraft.getInstance().player == null) return;

        ShaderInstance shader = RenderSystem.getShader();
        assert shader != null;

        if (!isRenderingLevel) {
            shader.safeGetUniform("SmallWaves").set(0F);
            shader.safeGetUniform("BigWaves").set(0F);
            shader.safeGetUniform("WiggleWaves").set(0F);
            shader.safeGetUniform("DistantWorldDeformation").set(0F);
            return;
        }

        DrugEffects drugEffects = Drug.getDrugEffects();

        shader.safeGetUniform("SmallWaves").set(drugEffects.SMALL_WAVES.getClamped());
        shader.safeGetUniform("BigWaves").set(drugEffects.BIG_WAVES.getClamped());
        shader.safeGetUniform("WiggleWaves").set(drugEffects.WIGGLE_WAVES.getClamped());
        shader.safeGetUniform("DistantWorldDeformation").set(drugEffects.WORLD_DEFORMATION.getValue());
    }
}
