package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.GlobalUniforms;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class Depth extends PostShader {
    public Depth() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/depth.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().HUE_AMPLITUDE.getValue() > EPSILON;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Amplitude", getDrugEffects().HUE_AMPLITUDE.getClamped());
        setUniform("TimePassed", GlobalUniforms.timePassed);
        process(partialTicks);
    }
}
