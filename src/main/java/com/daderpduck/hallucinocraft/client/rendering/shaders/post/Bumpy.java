package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class Bumpy extends PostShader {
    public Bumpy() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/bumpy.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().BUMPY.getValue() > EPSILON;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Intensity", getDrugEffects().BUMPY.getValue());
        process(partialTicks);
    }
}
