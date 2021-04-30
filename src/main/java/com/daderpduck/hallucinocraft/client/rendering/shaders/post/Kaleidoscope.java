package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.GlobalUniforms;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class Kaleidoscope extends PostShader {
    public Kaleidoscope() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/kaleidoscope.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().KALEIDOSCOPE_INTENSITY.getValue() > EPSILON;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Extend", getDrugEffects().KALEIDOSCOPE_INTENSITY.getValue());
        setUniform("TimePassed", GlobalUniforms.timePassed);
        setUniform("TimePassedSin", GlobalUniforms.timePassedSin);
        process(partialTicks);
    }
}
