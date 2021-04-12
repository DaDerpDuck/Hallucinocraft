package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class PostShaders {
    public static PostShader KALEIDOSCOPE;
    public static PostShader KALEIDOSCOPE2;
    public static PostShader COLOR;
    public static PostShader DEPTH;

    public static void init() throws IOException {
        KALEIDOSCOPE = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope.json"));
        KALEIDOSCOPE2 = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/kaleidoscope2.json"));
        COLOR = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/color.json"));
        DEPTH = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "shaders/post/depth.json"));
    }

    public static void cleanup() {
        if (KALEIDOSCOPE != null) KALEIDOSCOPE.close();
        KALEIDOSCOPE = null;

        if (KALEIDOSCOPE2 != null) KALEIDOSCOPE2.close();
        KALEIDOSCOPE2 = null;

        if (COLOR != null) COLOR.close();
        COLOR = null;

        if (DEPTH != null) DEPTH.close();
        DEPTH = null;
    }
}
