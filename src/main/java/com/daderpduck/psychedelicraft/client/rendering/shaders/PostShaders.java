package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class PostShaders {
    public static PostShader KALEIDOSCOPE;
    public static PostShader KALEIDOSCOPE2;
    public static PostShader COLOR;

    public static void init() throws IOException {
        KALEIDOSCOPE = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope.json"));
        KALEIDOSCOPE2 = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope2.json"));
        COLOR = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/color.json"));
    }
}
