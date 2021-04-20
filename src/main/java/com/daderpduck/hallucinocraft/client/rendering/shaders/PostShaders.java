package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class PostShaders {
    public static boolean useShaders = false;
    public static PostShader KALEIDOSCOPE;
    public static PostShader KALEIDOSCOPE2;
    public static PostShader COLOR;
    public static PostShader DEPTH;
    public static PostShader BUMPY;
    public static PostShader BLOOM;

    public static void init() throws IOException {
        KALEIDOSCOPE = new PostShader(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/kaleidoscope.json"));
        KALEIDOSCOPE2 = new PostShader(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/kaleidoscope2.json"));
        COLOR = new PostShader(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/color.json"));
        DEPTH = new PostShader(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/depth.json"));
        BUMPY = new PostShader(new ResourceLocation(Hallucinocraft.MOD_ID,"shaders/post/bumpy.json"));
        BLOOM = new PostShader(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/bloom.json"));

        useShaders = true;
    }

    public static void cleanup() {
        useShaders = false;

        if (KALEIDOSCOPE != null) KALEIDOSCOPE.close();
        KALEIDOSCOPE = null;

        if (KALEIDOSCOPE2 != null) KALEIDOSCOPE2.close();
        KALEIDOSCOPE2 = null;

        if (COLOR != null) COLOR.close();
        COLOR = null;

        if (DEPTH != null) DEPTH.close();
        DEPTH = null;

        if (BUMPY != null) BUMPY.close();
        BUMPY = null;

        if (BLOOM != null) BLOOM.close();
        BLOOM = null;
    }
}
