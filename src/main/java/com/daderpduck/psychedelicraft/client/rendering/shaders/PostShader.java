package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.mixin.client.AccessorShaderGroup;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class PostShader extends ShaderGroup {
    private final Map<String, float[]> uniformMap = new HashMap<>();
    private int width = 0;
    private int height = 0;

    public PostShader(ResourceLocation resourceLocation) throws IOException, JsonSyntaxException {
        super(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), resourceLocation);
    }

    public List<Shader> getShaders() {
        return ((AccessorShaderGroup)this).getPasses();
    }

    @Override
    public void process(float partialTicks) {
        Framebuffer framebuffer = Minecraft.getInstance().getMainRenderTarget();

        if (width != framebuffer.viewWidth || height != framebuffer.viewHeight) {
            this.resize(width = framebuffer.viewWidth, height = framebuffer.viewHeight);
        }

        uniformMap.forEach((name, floats) -> {
            switch (floats.length) {
                case 1:
                    for (Shader shader : getShaders()) {
                        shader.getEffect().safeGetUniform(name).set(floats[0]);
                    }
                    break;
                case 2:
                    for (Shader shader : getShaders()) {
                        shader.getEffect().safeGetUniform(name).set(floats[0], floats[1]);
                    }
                    break;
                case 3:
                    for (Shader shader : getShaders()) {
                        shader.getEffect().safeGetUniform(name).set(floats[0], floats[1], floats[2]);
                    }
                    break;
                case 4:
                    for (Shader shader : getShaders()) {
                        shader.getEffect().safeGetUniform(name).set(floats[0], floats[1], floats[2], floats[3]);
                    }
                    break;

            }
        });

        super.process(partialTicks);
    }

    public void setUniform(String uniformName, float v0) {
        if (uniformMap.containsKey(uniformName)) {
            float[] floats = uniformMap.get(uniformName);
            floats[0] = v0;
        } else {
            uniformMap.put(uniformName, new float[]{v0});
        }
    }

    public void setUniform(String uniformName, float v0, float v1) {
        if (uniformMap.containsKey(uniformName)) {
            float[] floats = uniformMap.get(uniformName);
            floats[0] = v0;
            floats[1] = v1;
        } else {
            uniformMap.put(uniformName, new float[]{v0, v1});
        }
    }

    public void setUniform(String uniformName, float v0, float v1, float v2) {
        if (uniformMap.containsKey(uniformName)) {
            float[] floats = uniformMap.get(uniformName);
            floats[0] = v0;
            floats[1] = v1;
            floats[2] = v2;
        } else {
            uniformMap.put(uniformName, new float[]{v0, v1, v2});
        }
    }

    public void setUniform(String uniformName, float v0, float v1, float v2, float v3) {
        if (uniformMap.containsKey(uniformName)) {
            float[] floats = uniformMap.get(uniformName);
            floats[0] = v0;
            floats[1] = v1;
            floats[2] = v2;
            floats[3] = v3;
        } else {
            uniformMap.put(uniformName, new float[]{v0, v1, v2, v3});
        }
    }
}
