package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.GlobalUniforms;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class Glitch extends PostShader {
    public Glitch() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/glitch.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().GLITCH.getValue() > EPSILON;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Amount", getDrugEffects().GLITCH.getValue());
        setUniform("TimePassed", GlobalUniforms.timePassed);
        process(partialTicks);
    }
}
