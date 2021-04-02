package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.mixin.client.AccessorShaderGroup;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class PostShader extends ShaderGroup {
    public PostShader(ResourceLocation resourceLocation) throws IOException, JsonSyntaxException {
        super(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), resourceLocation);
    }

    public List<Shader> getShaders() {
        return ((AccessorShaderGroup)this).getPasses();
    }
}
