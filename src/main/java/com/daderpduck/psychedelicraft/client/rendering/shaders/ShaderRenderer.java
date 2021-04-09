package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.client.rendering.DrugEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Psychedelicraft.MOD_ID)
public class ShaderRenderer {
    private static WorldShader shaderWorld;
    private static boolean activeShader = false;
    public static boolean useShader = true;

    public static void setup() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::setup);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        clear();

        try {
            PostShaders.init();
            shaderWorld = new WorldShader(mc.getResourceManager(), "psychedelicraft:world");
            shaderWorld.setSampler("texture", () -> 0);
            shaderWorld.setSampler("lightMap", () -> 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(ShaderRenderer::clear);
            return;
        }

        if (shaderWorld != null) shaderWorld.close();
        shaderWorld = null;

    }

    public static void startRenderPass() {
        if (!useShader) return;
        if (activeShader) return;
        activeShader = true;

        shaderWorld.clear();

        shaderWorld.safeGetUniform("modelViewMat").setMatrix(GlobalUniforms.modelView);
        shaderWorld.safeGetUniform("modelViewInverseMat").setMatrix(GlobalUniforms.modelViewInverse);
        shaderWorld.safeGetUniform("timePassed").setFloat(GlobalUniforms.timePassed);
        shaderWorld.safeGetUniform("fogMode").setInt(GlobalUniforms.fogMode);

        shaderWorld.safeGetUniform("smallWaves").setFloat(DrugEffects.SMALL_WAVES.getValue());
        shaderWorld.safeGetUniform("bigWaves").setFloat(DrugEffects.BIG_WAVES.getValue());
        shaderWorld.safeGetUniform("wiggleWaves").setFloat(DrugEffects.WIGGLE_WAVES.getValue());
        shaderWorld.safeGetUniform("distantWorldDeformation").setFloat(DrugEffects.WORLD_DEFORMATION.getValue());

        shaderWorld.apply();
    }

    public static void endRenderPass() {
        if (activeShader && !useShader) {
            shaderWorld.clear();
            activeShader = false;
            return;
        }

        if (!activeShader) return;
        activeShader = false;

        shaderWorld.clear();
    }

    public static WorldShader getWorldShader() {
        return shaderWorld;
    }

    public static boolean isActive() {
        return activeShader;
    }
}
