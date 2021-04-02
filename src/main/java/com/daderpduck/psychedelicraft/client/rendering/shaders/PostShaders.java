package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class PostShaders {
    public static PostShader KALEIDOSCOPE;

    public static void init() throws IOException {
        KALEIDOSCOPE = new PostShader(new ResourceLocation(Psychedelicraft.MOD_ID, "kaleidoscope"));
    }
}
