package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class Blur extends PostShader {
    public Blur() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/blur.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().BLUR.getValue() > EPSILON;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Radius", getDrugEffects().BLUR.getValue());
        process(partialTicks);
    }
}
